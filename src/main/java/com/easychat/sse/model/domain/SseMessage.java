package com.easychat.sse.model.domain;

import com.easychat.sse.enums.MessageType;

public interface SseMessage {
    MessageType getMessageType();

    String getContent();

    String getSender();
    String getReceiver();

    String getFileId();

}
