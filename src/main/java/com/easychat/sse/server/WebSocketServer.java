package com.easychat.sse.server;

import com.easychat.sse.config.WebsocketAutoConfig;
import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.model.dto.SocketMessage;
import com.easychat.sse.model.dto.TextMessage;
import com.easychat.sse.service.UserRelationService;
import com.easychat.sse.utils.ContextHolder;
import com.easychat.sse.utils.DateTimeFormatUtil;
import com.easychat.sse.utils.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
@ServerEndpoint(value = "/connect", configurator = WebsocketAutoConfig.class)
public class WebSocketServer {


    private final Map<String, Session> sessions = new ConcurrentHashMap<>(500);

    @OnOpen
    public void onOpen(Session session) {
        String userId = (String) session.getUserProperties().get("userId");
        sessions.putIfAbsent(userId, session);
        log.info("客户端：{}连接成功", session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        String userId = (String) session.getUserProperties().get("userId");

        Session s = sessions.remove(userId);
        try {
            s.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info("客户端：{}连接断开", session.getId());
    }

    @OnMessage
    public void onMsg(String message, Session session) {
        log.info("从客户端：{} 收到<--:{}", session.getId(), message);
        String userId = (String) session.getUserProperties().get("userId");
        SocketMessage socketMessage = ObjectMapperUtil.readValue(message, SocketMessage.class);
        String receiver = socketMessage.getReceiver();
        if (ObjectUtils.isEmpty(receiver)) return;

        TextMessage textMessage = TextMessage.builder()
                .content(socketMessage.getContent())
                .createTime(LocalDateTime.now().format(DateTimeFormatUtil.SLASH_DATE))
                .sender(userId)
                .build();
        sendMessage(textMessage, socketMessage.getReceiver());
    }

    public void sendMessage(Object message, String targetUser) {
        try {
            UserRelationService userRelationService = ContextHolder.getBean(UserRelationService.class);
            userRelationService.validateUserRelation(targetUser);
            String msg = ObjectMapperUtil.writeValueAsString(message);
            sessions.get(targetUser).getBasicRemote().sendText(msg);
            ContextHolder.publish(message);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new CustomRuntimeException(e.getMessage());
        }
    }

    /**
     * 自定义 指定的userId服务端向客户端发送消息
     */
    public void sendInfo(Object message, String toUserId) {
        if (ObjectUtils.isEmpty(message) || ObjectUtils.isEmpty(toUserId)) {
            return;
        }
        if (!ObjectUtils.isEmpty(toUserId)) {
            sendMessage(message, toUserId);
        }
    }

}
