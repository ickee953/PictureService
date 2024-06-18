package com.github.ickee953.service.pictures.controller;

import com.github.ickee953.service.pictures.entity.Picture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pictures")
public class PictureController {

    @PostMapping
    public Picture save(Picture picture) {
        return picture;
    }

}
