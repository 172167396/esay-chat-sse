package com.easychat.sse.model.dto;

import com.easychat.sse.enums.MessageType;
import com.easychat.sse.enums.RecentMsgType;
import com.easychat.sse.model.domain.SseMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshGroupMsg implements SseMessage {

    private String groupId;

    private String receiver;

    @Override
    public MessageType getMessageType() {
        return MessageType.REFRESH_GROUP_USER;
    }

    @Override
    public RecentMsgType getType() {
        return RecentMsgType.NOTICE;
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public String getSender() {
        return RecentMsgType.SYSTEM.name();
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
