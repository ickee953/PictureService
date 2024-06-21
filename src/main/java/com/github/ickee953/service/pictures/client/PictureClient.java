package com.github.ickee953.service.pictures.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class PictureClient {

    protected final static String URL_STORAGE_SERVICE = "http://172.17.0.2:8081";
    protected final static String METHOD_UPLOAD_FILE  = "/upload";
    protected final static String METHOD_GET_FILE     = "/files";

    private final RestTemplate restTemplate;

    public String uploadFile( MultipartFile file ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            body.add("files", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
        } catch (IOException e) {
            return null;
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        //todo get url from properties depend on development/release mode
        String serverUrl = URL_STORAGE_SERVICE + METHOD_UPLOAD_FILE;

        //todo catch java.net.ConnectException
        ResponseEntity<String> response = restTemplate
                .postForEntity(serverUrl, requestEntity, String.class);

        if( response.getStatusCode() == HttpStatus.OK ){
            return METHOD_GET_FILE + "/" + response.getBody();
        } else {
            return null;
        }
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
