package com.easychat.sse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {

    CONNECT(1,"用户加入"),
    PUSH_MESSAGE(2,"发言"),
    CLOSE(3,"下线");


    private int type;
    private String desc;
}
