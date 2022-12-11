package com.easychat.sse.model.domain;

import com.easychat.sse.enums.MessageType;
import com.easychat.sse.enums.RecentMsgType;

public interface SseMessage {
    MessageType getMessageType();
    RecentMsgType getType();

    String getContent();

    String getSender();
    String getReceiver();

    String getFileId();

}
