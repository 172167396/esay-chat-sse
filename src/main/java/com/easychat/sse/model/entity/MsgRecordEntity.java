package com.easychat.sse.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@TableName("message_record")
public class MsgRecordEntity {
    private String id;
    private String content;
    private String senderId;
    private int messageType;
    private String receiverId;
    private String fileId;
    private String belongRecentId;
    private LocalDateTime createTime;
}
