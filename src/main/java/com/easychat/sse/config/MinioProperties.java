package com.easychat.sse.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private String endPoint;
    private String accessKey;
    private String secretKey;

    private static MinioProperties instance;

    @PostConstruct
    public void init() {
        instance = this;
    }

    public static String getUrl() {
        return instance.getEndPoint();
    }
}
