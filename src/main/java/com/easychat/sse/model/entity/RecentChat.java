package com.easychat.sse.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@TableName("recent_chat")
@Getter
@Setter
public class RecentChat {
    private String id;
    private int type;
    private String userId;
    private LocalDateTime lastActiveTime;
    private String targetId;
    private LocalDateTime createTime;
}
