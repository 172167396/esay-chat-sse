package com.easychat.sse.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocketMessage {
    private String content;
    private String receiver;
    private int messageType;
}
