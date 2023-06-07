package com.easychat.sse.model.dto;

import com.easychat.sse.model.entity.MsgRecordEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class MsgRecordDTO extends MsgRecordEntity {
    private String userName;
    private String bucket;
    private String fileName;
}
