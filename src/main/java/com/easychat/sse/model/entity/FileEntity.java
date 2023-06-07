package com.easychat.sse.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@TableName("sse_file")
@Getter
@Setter
public class FileEntity {

    @TableId("id")
    private String id;

    @TableField("bucket")
    private String bucket;

    @TableField("file_name")
    private String fileName;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("create_user")
    private String createUser;

    @TableField("type")
    private int type;
}
