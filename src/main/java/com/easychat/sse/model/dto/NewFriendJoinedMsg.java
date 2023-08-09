package com.easychat.sse.model.dto;

import com.easychat.sse.enums.MessageType;
import com.easychat.sse.enums.RecentMsgType;
import com.easychat.sse.model.domain.SseMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewFriendJoinedMsg implements SseMessage {

    private String friendId;

    private String receiver;

    private String sender;

    @Override
    public MessageType getMessageType() {
        return MessageType.NEW_FRIEND_JOINED;
    }

    @Override
    public RecentMsgType getType() {
        return RecentMsgType.NOTICE;
    }

    @Override
    public String getContent() {
        return "我们已经是好友啦~";
    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public String getReceiver() {
        return receiver;
    }

    @Override
    public String getFileId() {
        return null;
    }
}
