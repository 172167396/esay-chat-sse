package com.easychat.sse.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class MessageEntity {
    private String createUserId;
    private LocalDateTime sendTime;
    private String content;
    private int messageType;
}
