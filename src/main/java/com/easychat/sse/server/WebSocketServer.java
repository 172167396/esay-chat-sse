package com.easychat.sse.server;

import cn.hutool.core.net.URLDecoder;
import com.easychat.sse.config.WebsocketAutoConfig;
import com.easychat.sse.enums.RecentMsgType;
import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.model.domain.SseMessage;
import com.easychat.sse.model.domain.UserDomain;
import com.easychat.sse.model.dto.SimpleFriendDTO;
import com.easychat.sse.model.dto.SocketMessage;
import com.easychat.sse.model.dto.TextMessage;
import com.easychat.sse.service.UserRelationService;
import com.easychat.sse.utils.ContextHolder;
import com.easychat.sse.utils.DateTimeFormatUtil;
import com.easychat.sse.utils.MinioUtil;
import com.easychat.sse.utils.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.easychat.sse.utils.DateTimeFormatUtil.getChatLineDisplayDate;


@Slf4j
@Component
@ServerEndpoint(value = "/connect", configurator = WebsocketAutoConfig.class)
public class WebSocketServer {

    private static ApplicationContext applicationContext;


    @OnOpen
    public void onOpen(Session session) {
        String userId = (String) session.getUserProperties().get("userId");
        SessionUtil.putIfAbsent(userId, session);
        log.info("客户端：{}连接成功", session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        String userId = (String) session.getUserProperties().get("userId");

        Session s = SessionUtil.remove(userId);
        try {
            s.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info("客户端：{}连接断开", session.getId());
    }

    @OnMessage
    public void onMsg(String message, Session session) {
        log.info("从客户端：{} 收到-->:{}", session.getId(), message);
        UserDomain userDomain = (UserDomain) session.getUserProperties().get("userId");
        SecurityManager securityManager = (SecurityManager) session.getUserProperties().get("securityManager");
        SecurityUtils.setSecurityManager(securityManager);
        SocketMessage socketMessage = ObjectMapperUtil.readValue(message, SocketMessage.class);
        String receiver = socketMessage.getReceiver();
        if (ObjectUtils.isEmpty(receiver)) return;
        String decodeMsg = URLDecoder.decode(socketMessage.getContent(), StandardCharsets.UTF_8);
        TextMessage textMessage = TextMessage.builder()
                .content(decodeMsg)
                .createTime(getChatLineDisplayDate(LocalDateTime.now()))
                .sender(userDomain.getId())
                .name(userDomain.getName())
                .receiver(receiver)
                .type(RecentMsgType.PERSONAL)
                .build();
        sendMessage(textMessage, socketMessage.getReceiver(), false);
    }

    public <T extends SseMessage> void sendMessage(T message, String targetUser, boolean system) {
        try {
            if (!system) {
                UserRelationService userRelationService = applicationContext.getBean(UserRelationService.class);
                SimpleFriendDTO simpleFriendDTO = userRelationService.validateUserRelation(message.getSender(), targetUser);
                if (message instanceof TextMessage) {
                    ((TextMessage) message).setSenderAvatar(MinioUtil.buildPath(simpleFriendDTO.getAvatarPath()));
                    ((TextMessage) message).setLastActiveTime(DateTimeFormatUtil.getChatLineDisplayDate(LocalDateTime.now()));
                    ((TextMessage) message).setName(simpleFriendDTO.getName());
                }
            }
            String msg = ObjectMapperUtil.writeValueAsString(message);
            Session targetSession = SessionUtil.get(targetUser);
            if (targetSession != null) {
                targetSession.getBasicRemote().sendText(msg);
            }
            ContextHolder.publish(message);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new CustomRuntimeException(e.getMessage());
        }
    }


    /**
     * 自定义 指定的userId服务端向客户端发送消息
     */
    public <T extends SseMessage> void sendInfo(T message, String toUserId, boolean system) {
        if (ObjectUtils.isEmpty(message) || ObjectUtils.isEmpty(toUserId)) {
            return;
        }
        if (!ObjectUtils.isEmpty(toUserId)) {
            sendMessage(message, toUserId, system);
        }
    }


    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketServer.applicationContext = applicationContext;
    }
}
