package com.easychat.sse.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestOfFriend {
    private String id;
    private String applyUser;
    private String receiveUser;
    private int state;
    private String remark;
    private LocalDateTime createTime;
}
