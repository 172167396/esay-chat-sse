package com.easychat.sse;

import com.easychat.sse.server.WebSocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

@EnableAsync
@SpringBootApplication
@MapperScan(basePackages = "com.easychat.sse",annotationClass = Repository.class)
public class SseApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SseApplication.class, args);
        WebSocketServer.setApplicationContext(context);
    }

}
