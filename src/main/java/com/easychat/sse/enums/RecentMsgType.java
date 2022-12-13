package com.easychat.sse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecentMsgType {
    /**
     * 群聊
     */
    GROUP,
    /**
     * 个人聊天
     */
    PERSONAL,
    /**
     * 系统通知
     */
    NOTICE,
    SYSTEM,
    /**
     * 好友申请
     */
    NEW_FRIEND;
}
