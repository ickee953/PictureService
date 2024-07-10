/**
 * © Panov Vitaly 2024 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.github.ickee953.micros.pictures.config;

import com.github.ickee953.micros.pictures.client.PictureClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Configuration
public class ClientConfig {

    @Bean
    public RestTemplate restTemplate(
            @Value(value = "${service.storage.username}") String username,
            @Value(value = "${service.storage.password}") String password
    ){
        RestTemplate restTemplate = new RestTemplate();
        //BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //restTemplate.setInterceptors(
        //        List.of( new BasicAuthenticationInterceptor(username, encoder.encode(password)) ));
        return restTemplate;
    }

    /*@Bean
    public PictureClient pictureClient(
            @Value(value = "${service.storage.url.base}") String baseUrl,
            @Value(value = "${service.storage.username}") String username,
            @Value(value = "${service.storage.password}") String password,
            @Value(value = "${service.storage.url.upload}") String storageServiceUrlUpload,
            @Value(value = "${service.storage.url.files}") String storageServiceUrlFiles
    ) {
        return new PictureClient(
                storageServiceUrlUpload,
                storageServiceUrlFiles,
                RestClient.builder()
                        .baseUrl(baseUrl)
                        .requestInterceptor(
                               new BasicAuthenticationInterceptor(username, password)
                        )
                        .build()
        );
    }*/
}
