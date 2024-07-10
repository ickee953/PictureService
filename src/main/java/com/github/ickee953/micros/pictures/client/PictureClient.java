/**
 * © Panov Vitaly 2024 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.github.ickee953.micros.pictures.client;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PictureClient {

    private final String storageServiceUrl;
    private final String storageServiceUrlUpload;
    private final String storageServiceUrlFiles;
    private final String username;
    private final String password;
    private final RestTemplate restTemplate;

    @Autowired
    public PictureClient(
            @Value(value = "${service.storage.url.base}") String storageServiceUrl,
            @Value(value = "${service.storage.url.upload}") String storageServiceUrlUpload,
            @Value(value = "${service.storage.url.files}") String storageServiceUrlFiles,
            @Value(value = "${service.storage.username}") String username,
            @Value(value = "${service.storage.password}") String password,
            RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.storageServiceUrl = storageServiceUrl;
        this.storageServiceUrlUpload = storageServiceUrlUpload;
        this.storageServiceUrlFiles = storageServiceUrlFiles;
        this.username = username;
        this.password = password;
    }

    /**
     * throws IOException then can't get input stream from MultipartFile function param.
     * */
    public String uploadFile( MultipartFile file ) throws IOException {
        InputStream inputStream = file.getInputStream();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("storage_service_user", "password");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("files", new MultipartInputStreamFileResource(inputStream, file.getOriginalFilename()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String serverUrl = storageServiceUrl + storageServiceUrlUpload;

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return storageServiceUrlFiles + "/" + response.getBody();
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace(System.err);
        }

        return null;
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
