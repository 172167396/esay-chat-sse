package com.easychat.sse.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@TableName("message_record")
public class MsgRecordEntity {
    private String id;
    private String content;
    private String senderId;
    private int messageType;
    private String receiverId;
    private String fileId;
    private LocalDateTime createTime;
}
