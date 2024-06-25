/**
 * © Panov Vitaly 2024 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.github.ickee953.micros.pictures.service;

import com.github.ickee953.micros.pictures.client.PictureClient;
import com.github.ickee953.micros.pictures.dto.PictureDto;
import com.github.ickee953.micros.pictures.entity.Picture;
import com.github.ickee953.micros.pictures.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Phaser;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class PictureService {

    protected abstract static class UploadFileOrdered implements Runnable {

        protected int fileIndex;

        UploadFileOrdered( int index ){
            this.fileIndex = index;
        }

    }

    private final PictureClient pictureClient;

    private final PictureRepository pictureRepository;

    public Iterable<Picture> getAll() {
        return pictureRepository.findAll();
    }

    public Picture add(PictureDto picture) {
        return pictureRepository.save( new Picture()
                .setUrl(picture.getFilename())
                .setCreatedAt(LocalDateTime.now())
        );
    }

    public List<String> uploadFiles( List<MultipartFile> files ) {
        int currentPicNum           = 0;
        List<String> uploadedUrls   = new LinkedList<>();
        Lock locker                 = new ReentrantLock();
        Condition condition         = locker.newCondition();
        Phaser phase                = new Phaser(1);

        for (MultipartFile file : files) {                                    //upload files with multithreads
            phase.register();
            List<String> finalUploadedUrls = uploadedUrls;
            new Thread(new UploadFileOrdered(currentPicNum) {
                @Override
                public void run() {
                    String uploadedFilename = null;
                    try {
                        uploadedFilename = pictureClient.uploadFile(file);

                        locker.lock();
                        try {
                            while (this.fileIndex > finalUploadedUrls.size())
                                condition.await();

                            condition.signalAll();
                        } catch (InterruptedException e) {
                            e.printStackTrace(System.err);
                        } finally {
                            finalUploadedUrls.add(uploadedFilename);
                            locker.unlock();
                            phase.arriveAndDeregister();
                        }

                    } catch (IOException e) {
                        e.printStackTrace(System.err);
                        //todo add currentPicNum file with file.getOriginalFilename() to errors list
                    }
                }
            }).start();

            currentPicNum++;
        }
        phase.arriveAndAwaitAdvance();                                           //wait while files uploads
        uploadedUrls = uploadedUrls.stream().filter(Objects::nonNull).toList();  //remove null elements
        phase.arriveAndDeregister();

        return uploadedUrls;
    }

}
