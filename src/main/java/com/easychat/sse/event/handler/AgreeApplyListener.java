package com.easychat.sse.event.handler;

import com.easychat.sse.model.dto.NewFriendJoinedMsg;
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

        RefreshGroupMsg applierMsg = new RefreshGroupMsg();
        applierMsg.setGroupId(userRelationDTO.getApplyGroup());
        applierMsg.setReceiver(userRelationDTO.getUserId());
        //刷新申请人的好友列表
        webSocketServer.sendMessage(applierMsg, applierMsg.getReceiver(), true);
//        //向申请人插入一条"我们已经是好友啦"的消息,以接收人的角度发送
//        NewFriendJoinedMsg friendJoinedMsg = new NewFriendJoinedMsg();
//        friendJoinedMsg.setReceiver(userRelationDTO.getUserId());
//        friendJoinedMsg.setFriendId(userRelationDTO.getFriendId());
//        friendJoinedMsg.setSender(userRelationDTO.getFriendId());
//        webSocketServer.sendMessage(applierMsg, applierMsg.getReceiver(), false);


        RefreshGroupMsg agreeMsg = new RefreshGroupMsg();
        agreeMsg.setGroupId(userRelationDTO.getAgreeGroup());
        agreeMsg.setReceiver(userRelationDTO.getFriendId());
        //刷新申请人的好友列表
        webSocketServer.sendMessage(agreeMsg, agreeMsg.getReceiver(), true);
    }
}
