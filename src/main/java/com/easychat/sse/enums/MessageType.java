package com.easychat.sse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {

    TEXT("1","文本"),
    EMOJI("2","表情"),
    FILE("3","文件");

    private String type;
    private String name;
}
