package com.easychat.sse.server;

import com.easychat.sse.utils.ObjectMapperUtil;
import com.easychat.sse.utils.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static com.easychat.sse.shiro.ShiroUtil.getUserId;

@Slf4j
@Component
@ServerEndpoint("/connect")
public class WebSocketServer {

    private static final String key = "userSession:";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session) {
        RedisUtil.set(key + getUserId(), session);
        log.info("客户端：{}连接成功", session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        String id = getUserId();
        RedisUtil.del(key + id);
        log.info("客户端：{}连接断开", session.getId());
    }

    @OnMessage
    public String onMsg(String message, Session session) {
        log.info("从客户端：{} 收到<--:{}", session.getId(), message);
        String send = message.toUpperCase();
        String result = "客户：%s您好，来自server 的消息:%s";
        result = String.format(result, session.getId(), send);
        return "来自server 的消息：" + result;
    }

    public static void sendMessage(Object message, String targetUser) {
        try {
            String msg = ObjectMapperUtil.writeValueAsString(message);
            RedisUtil.getSession(key + targetUser).getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义 指定的userId服务端向客户端发送消息
     */
    public static void sendInfo(Object message, String toUserId) {
        if (ObjectUtils.isEmpty(message) || ObjectUtils.isEmpty(toUserId)) {
            return;
        }
        if (!ObjectUtils.isEmpty(toUserId)) {
            sendMessage(message, toUserId);
        }
    }

}
