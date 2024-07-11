package com.github.ickee953.micros.pictures.service;

import com.github.ickee953.micros.pictures.dto.PictureDto;
import com.github.ickee953.micros.pictures.entity.Picture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PictureService {

    Iterable<Picture> getAll();

    Page<Picture> getAll(Pageable pageable);

    Picture create(PictureDto picture);

}
