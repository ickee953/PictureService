package com.github.ickee953.micros.pictures.messages;

import com.github.ickee953.micros.pictures.common.Base64DecodedMultipartFile;
import com.github.ickee953.micros.pictures.dto.PictureDto;
import com.github.ickee953.micros.pictures.entity.Picture;
import com.github.ickee953.micros.pictures.service.DefaultPictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@RequiredArgsConstructor
public class UploadFileConsumer {

    private static final String KAFKA_TOPIC = "file-uploaded-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;;

    private final DefaultPictureService pictureService;

    @KafkaListener(topics = {"file-upload-topic"})
    public void uploadFile(ConsumerRecord<String, byte[]> record){
        log.info("received message : {}", record);

        assert record.value() != null;

        MultipartFile file = new Base64DecodedMultipartFile("pic.jpg", record.value());

        List<String> uploadedFilesPaths = pictureService.uploadFiles(List.of(file));

        uploadedFilesPaths.forEach( url -> {
            PictureDto dto = new PictureDto(url);

            Picture picture = pictureService.create( dto );

            try {
                kafkaTemplate.send(KAFKA_TOPIC, record.key(), picture.getPath() ).get();
            } catch (InterruptedException | ExecutionException e) {
                log.error(String.format("Failed to send uploaded picture kafka message in topic: %s with key: %s",
                        KAFKA_TOPIC, picture.getId()));
            }
        } );
    }

}
