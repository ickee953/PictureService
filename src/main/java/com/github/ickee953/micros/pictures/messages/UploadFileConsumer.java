package com.github.ickee953.micros.pictures.messages;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class UploadFileConsumer {

    @KafkaListener(topics = {"file-upload-topic"})
    public void uploadFile(ConsumerRecord<UUID, byte[]> record){
        log.info("received message : {}", record);
    }

}
