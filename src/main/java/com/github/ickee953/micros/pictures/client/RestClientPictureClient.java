package com.github.ickee953.micros.pictures.client;

import org.springframework.http.client.support.HttpAccessor;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class RestClientPictureClient extends AbstractPictureClient {

    private final RestClient restClient;

    public RestClientPictureClient(
            String storageServiceUrl,
            String storageServiceUrlUpload,
            String storageServiceUrlFiles,
            String username,
            String password,
            RestClient restClient
    ) {
        super(
                storageServiceUrl,
                storageServiceUrlUpload,
                storageServiceUrlFiles,
                username,
                password
        );
        this.restClient = restClient;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        //todo not implemented
        return "";
    }
}

