package com.github.ickee953.micros.pictures.client;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractPictureClient implements FileClient {

    protected final String storageServiceUrl;
    protected final String storageServiceUrlUpload;
    protected final String storageServiceUrlFiles;
    protected final String username;
    protected final String password;

    protected AbstractPictureClient(
            String storageServiceUrl,
            String storageServiceUrlUpload,
            String storageServiceUrlFiles,
            String username,
            String password
    ) {
        this.storageServiceUrl = storageServiceUrl;
        this.storageServiceUrlUpload = storageServiceUrlUpload;
        this.storageServiceUrlFiles = storageServiceUrlFiles;
        this.username = username;
        this.password = password;
    }

    protected static class MultipartInputStreamFileResource extends InputStreamResource {

        private final String filename;

        MultipartInputStreamFileResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() throws IOException {
            return -1; // we do not want to generally read the whole stream into memory ...
        }
    }
}
