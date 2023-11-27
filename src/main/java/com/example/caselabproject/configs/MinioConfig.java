package com.example.caselabproject.configs;

import io.minio.MinioClient;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Configuration
public class MinioConfig {

    @org.springframework.beans.factory.annotation.Value("${minio.url}")
    private String minioUrl;

    @org.springframework.beans.factory.annotation.Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }
}
