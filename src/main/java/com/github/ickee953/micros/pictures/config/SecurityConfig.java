package com.github.ickee953.micros.pictures.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeHttpRequests(authorizeRequests->
                        authorizeRequests.requestMatchers("/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/pictures").hasRole("SERVICE")
                        )
                .build();
    }

}
