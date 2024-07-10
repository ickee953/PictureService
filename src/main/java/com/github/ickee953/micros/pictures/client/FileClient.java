package com.github.ickee953.micros.pictures.client;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileClient {

    /**
     * throws IOException then can't get input stream from function param.
     * */
    String uploadFile( MultipartFile file ) throws IOException;

}
