package com.easychat.sse.model.dto;

import com.easychat.sse.model.entity.MsgRecordEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MsgRecordDTO extends MsgRecordEntity {
    private String userName;
    private String bucket;
    private String fileName;
}
