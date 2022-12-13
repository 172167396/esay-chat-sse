package com.easychat.sse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FriendApplySource {

    FRIEND_SEARCH(1,"来自好友查找"),;

    private final int source;
    private final String name;
}
