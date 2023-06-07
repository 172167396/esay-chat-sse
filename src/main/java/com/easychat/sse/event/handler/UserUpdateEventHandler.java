package com.easychat.sse.event.handler;

import com.easychat.sse.event.UserUpdateEvent;
import com.easychat.sse.model.dto.RefreshGroupMsg;
import com.easychat.sse.model.dto.UserRelationDTO;
import com.easychat.sse.server.WebSocketServer;
import com.easychat.sse.shiro.CustomRealm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
public class UserUpdateEventHandler {

    @Resource
    CustomRealm customRealm;

    @Async
    @EventListener(value = UserUpdateEvent.class)
    public void handle(UserUpdateEvent userUpdateEvent) {
        customRealm.resetPrincipalInfo(userUpdateEvent.getUser());
    }
}
