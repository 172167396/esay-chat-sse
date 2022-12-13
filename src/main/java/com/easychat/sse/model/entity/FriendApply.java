package com.easychat.sse.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendApply {
    private String id;
    private String applyUser;
    private String applyUserName;
    private String receiveUser;
    private String groupId;
    private int state;
    /**
     * 验证信息
     */
    private String remark;
    /**
     * 备注
     */
    private String nickName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
