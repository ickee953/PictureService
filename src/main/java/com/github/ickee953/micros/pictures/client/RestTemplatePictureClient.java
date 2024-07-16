/**
 * © Panov Vitaly 2024 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.github.ickee953.micros.pictures.client;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Deprecated
@Component
public class RestTemplatePictureClient extends AbstractPictureClient {

    private final RestTemplate restTemplate;

    public RestTemplatePictureClient(
            String storageServiceUrl,
            String storageServiceUrlUpload,
            String storageServiceUrlFiles,
            String username,
            String password,
            RestTemplate restTemplate
    ) {
        super(
                storageServiceUrl,
                storageServiceUrlUpload,
                storageServiceUrlFiles,
                username,
                password
        );
        this.restTemplate = restTemplate;
    }

    @Override
    public String uploadFile( MultipartFile file ) throws IOException {
        InputStream inputStream = file.getInputStream();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
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

}
