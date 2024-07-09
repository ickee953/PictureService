/**
 * © Panov Vitaly 2024 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.github.ickee953.micros.pictures.controller;

import com.github.ickee953.micros.pictures.dto.PictureDto;
import com.github.ickee953.micros.pictures.entity.Picture;
import com.github.ickee953.micros.pictures.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/pictures")
@RequiredArgsConstructor
public class PictureController {

    private final PictureService pictureService;

    @PostMapping
    public ResponseEntity<Iterable<Picture>> save(@RequestPart(name = "files", required = true) List<MultipartFile> pics) {
        List<String> uploadedFilesPaths = pictureService.uploadFiles(pics);

        uploadedFilesPaths.forEach( url -> {
            PictureDto dto = new PictureDto(url);

            pictureService.create( dto );
        } );


        return ResponseEntity.ok(pictureService.getAll());
    }

    @GetMapping
    public ResponseEntity<Iterable<Picture>> getAll(){
        try {
            return ResponseEntity.ok(pictureService.getAll());

        } catch( Exception e ) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/page/{page}/{size}")
    public ResponseEntity<Page<Picture>> getPictures(
            @PathVariable int page, @PathVariable int size
    ) {
        try {
            return ResponseEntity.ok(pictureService.getAll(PageRequest.of(page, size)));

        } catch( Exception e ) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
