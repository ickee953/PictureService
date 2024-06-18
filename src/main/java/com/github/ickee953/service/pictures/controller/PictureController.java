package com.github.ickee953.service.pictures.controller;

import com.github.ickee953.service.pictures.dto.PictureDto;
import com.github.ickee953.service.pictures.entity.Picture;
import com.github.ickee953.service.pictures.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pictures")
@RequiredArgsConstructor
public class PictureController {

    private final PictureService pictureService;

    @PostMapping
    public Picture save(@RequestBody PictureDto picture) {
        return pictureService.save(picture);
    }

    @GetMapping
    public Iterable<Picture> getAll(){
        return pictureService.getAll();
    }

}
