package com.easychat.sse.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@TableName("sys_user_relations")
public class UserRelation {
    private String id;
    private String userId;
    private String friendId;
    private String remarkName;
    private String groupId;
    private LocalDateTime joinTime;
}
