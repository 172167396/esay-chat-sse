package com.easychat.sse.listener;

import com.easychat.sse.model.entity.UserRelation;
import com.easychat.sse.server.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class AgreeApplyListener {

    @EventListener(value = UserRelation.class)
    public void handle(UserRelation userRelation) {
        log.info(userRelation.toString());

    }
}
