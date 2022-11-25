package com.easychat.sse.model.dto;

import com.easychat.sse.model.entity.FriendApply;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendApplyDTO extends FriendApply {
    private String userName;
    private String bucket;
    private String fileName;
}
