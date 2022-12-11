package com.easychat.sse.config;

import com.easychat.sse.shiro.ShiroUtil;
import com.easychat.sse.utils.ContextHolder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Configuration
public class WebsocketAutoConfig extends ServerEndpointConfig.Configurator {

    /**
     * 解决onOpen无法使用getUser()
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 将用户信息存储到socket的配置里
        sec.getUserProperties().put("user", ShiroUtil.getUser());
        sec.getUserProperties().put("userId", ShiroUtil.getUserId());
        super.modifyHandshake(sec, request, response);
    }

    @Bean
    public ServerEndpointExporter endpointExporter() {
        return new ServerEndpointExporter();
    }
}
