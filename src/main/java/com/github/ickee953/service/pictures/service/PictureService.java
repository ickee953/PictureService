package com.github.ickee953.service.pictures.service;

import com.github.ickee953.service.pictures.dto.PictureDto;
import com.github.ickee953.service.pictures.entity.Picture;
import com.github.ickee953.service.pictures.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PictureService {

    private final PictureRepository pictureRepository;

    public Picture save(PictureDto pictureDto) {
        //todo save file and get file path
        String path = "test_path";

        Picture picture = new Picture()
                .setId(UUID.randomUUID().toString())
                .setUrl(path)
                .setCreatedAt(LocalDateTime.now());

        return pictureRepository.save(picture);
    }

    public Iterable<Picture> getAll() {
        return pictureRepository.findAll();
    }

}
