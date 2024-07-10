/**
 * © Panov Vitaly 2024 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.github.ickee953.micros.pictures.config;

import com.github.ickee953.micros.pictures.client.RestTemplatePictureClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public RestTemplatePictureClient restTemplatePictureClient(
            @Value(value = "${service.storage.url.base}") String baseUrl,
            @Value(value = "${service.storage.username}") String username,
            @Value(value = "${service.storage.password}") String password,
            @Value(value = "${service.storage.url.upload}") String storageServiceUrlUpload,
            @Value(value = "${service.storage.url.files}") String storageServiceUrlFiles
    ) {
        return new RestTemplatePictureClient(
                baseUrl,
                storageServiceUrlUpload,
                storageServiceUrlFiles,
                username,
                password,
                restTemplate()
        );
    }
}
