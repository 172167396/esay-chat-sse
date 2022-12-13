package com.easychat.sse.event.handler;

import com.easychat.sse.enums.RecentMsgType;
import com.easychat.sse.model.dto.TextMessage;
import com.easychat.sse.model.entity.FriendApply;
import com.easychat.sse.server.WebSocketServer;
import com.easychat.sse.utils.DateTimeFormatUtil;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FriendApplyHandler {

    @Resource
    WebSocketServer webSocketServer;

    @Async
    @EventListener(value = FriendApply.class)
    public void handleFriendApply(FriendApply friendApply) {
        String chatLineDisplayDate = DateTimeFormatUtil.getChatLineDisplayDate(friendApply.getCreateTime());
        TextMessage textMessage = TextMessage.builder().sender(RecentMsgType.NEW_FRIEND.name())
                .lastActiveTime(chatLineDisplayDate)
                .content(friendApply.getApplyUserName() + "申请添加你为好友")
                .createTime(chatLineDisplayDate)
                .name("新朋友")
                .type(RecentMsgType.NEW_FRIEND)
                .receiver(friendApply.getReceiveUser())
                .build();
        webSocketServer.sendMessage(textMessage, friendApply.getReceiveUser(), true);
    }
}
