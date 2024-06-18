package com.github.ickee953.service.pictures.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Picture {

    @Id
    private String id;
    private String url;
    private LocalDateTime createdAt;

}
