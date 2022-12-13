package com.easychat.sse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {

    TEXT(1, "文本"),
    EMOJI(2, "表情"),
    FILE(3, "文件"),
    REFRESH_GROUP_USER(4, "刷新列表好友"),
    DELETE_USER(5, "删除好友"),
    ;

    private final int type;
    private final String name;
}
