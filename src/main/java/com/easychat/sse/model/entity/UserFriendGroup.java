package com.easychat.sse.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserFriendGroup {
    private String id;
    private String name;
    private String createUser;
    private LocalDateTime createTime;
}
