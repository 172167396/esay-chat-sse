package com.easychat.sse.listener;

import com.easychat.sse.model.dto.RefreshGroupMsg;
import com.easychat.sse.model.dto.UserRelationDTO;
import com.easychat.sse.model.entity.UserRelation;
import com.easychat.sse.server.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
public class AgreeApplyListener {

    @Resource
    WebSocketServer webSocketServer;

    @Async
    @EventListener(value = UserRelationDTO.class)
    public void handle(UserRelationDTO userRelationDTO) {
        //同时刷新申请人和接收人的指定好友列表
        RefreshGroupMsg applierMsg = new RefreshGroupMsg();
        applierMsg.setGroupId(userRelationDTO.getApplyGroup());
        applierMsg.setReceiver(userRelationDTO.getUserId());
        webSocketServer.sendMessage(applierMsg, applierMsg.getReceiver(), true);

        RefreshGroupMsg agreeMsg = new RefreshGroupMsg();
        agreeMsg.setGroupId(userRelationDTO.getAgreeGroup());
        agreeMsg.setReceiver(userRelationDTO.getFriendId());
        webSocketServer.sendMessage(agreeMsg, agreeMsg.getReceiver(), true);
    }
}
