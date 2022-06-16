package com.easychat.sse.model.domain;

import com.easychat.sse.model.entity.MessageEntity;
import com.easychat.sse.enums.MessageType;

public interface SseMessage {

    MessageType getMessageType();

    MessageEntity toMessage();
}
