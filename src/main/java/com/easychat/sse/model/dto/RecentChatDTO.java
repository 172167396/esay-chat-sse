package com.easychat.sse.model.dto;

import com.easychat.sse.model.entity.RecentChat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecentChatDTO extends RecentChat {
    private String briefMsg;
    private String avatarPath;
    /**
     * 好友的真实网名
     */
    private String targetUserName;
    /**
     * 好友的备注姓名
     */
    private String remarkName;
}
