package com.github.ickee953.micros.pictures.messages;

import com.github.ickee953.micros.pictures.common.Base64DecodedMultipartFile;
import com.github.ickee953.micros.pictures.dto.PictureDto;
import com.github.ickee953.micros.pictures.entity.Picture;
import com.github.ickee953.micros.pictures.service.DefaultPictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@RequiredArgsConstructor
public class UploadFileConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;;

    @Value(value = "${app.kafka.topic.file-uploaded}")
    private String fileUploadedTopic;

    private final DefaultPictureService fileService;

    @Value(value = "${app.default-upload-filename}")
    private String defaultFileName;

    @KafkaListener(topics = {"${app.kafka.topic.file-upload}"})
    public void uploadFile(ConsumerRecord<String, byte[]> record){
        log.info("received message : {}", record);

        assert record.value() != null;

        MultipartFile file = new Base64DecodedMultipartFile(defaultFileName, record.value());

        List<String> uploadedFilesPaths = fileService.uploadFiles(List.of(file));

        uploadedFilesPaths.forEach( url -> {
            PictureDto dto = new PictureDto(url);

            Picture picture = fileService.create( dto );

            try {
                kafkaTemplate.send(fileUploadedTopic, record.key(), picture.getPath() ).get();
            } catch (InterruptedException | ExecutionException e) {
                log.error(String.format("Failed to send uploaded picture kafka message in topic: %s with key: %s",
                        fileUploadedTopic, picture.getId()));
            }
        } );
    }

}
