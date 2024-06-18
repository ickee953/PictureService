package com.github.ickee953.service.pictures.repository;

import com.github.ickee953.service.pictures.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, String> {

}
