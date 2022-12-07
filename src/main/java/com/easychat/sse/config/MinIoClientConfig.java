package com.easychat.sse.config;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Slf4j
@Configuration
public class MinIoClientConfig {

    @Resource
    MinioProperties minioProperties;

    /**
     * 注入minio 客户端
     *
     * @return MinioClient
     */
    @Bean
    public MinioClient minioClient() {
        log.info("minio url is:{},accessKey is: {},secretKey is: {}",minioProperties.getEndPoint(),minioProperties.getAccessKey(),minioProperties.getSecretKey());
        return MinioClient.builder()
                .endpoint(minioProperties.getEndPoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}