/**
 * © Panov Vitaly 2024 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.github.ickee953.micros.pictures.controller;

import com.github.ickee953.micros.pictures.entity.Picture;
import com.github.ickee953.micros.pictures.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/pictures")
@RequiredArgsConstructor
public class PictureController {

    private final PictureService pictureService;

    @PostMapping
    public List<Picture> save(@RequestPart(name = "files", required = true) List<MultipartFile> pics) {
        return pictureService.uploadFiles(pics);
    }

    @GetMapping
    public Iterable<Picture> getAll(){
        return pictureService.getAll();
    }

}
