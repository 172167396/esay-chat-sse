package com.easychat.sse.model.dto;

import com.easychat.sse.model.entity.UserRelation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRelationDTO extends UserRelation {
    /**
     * 申请人选择的好友列表
     */
    private String applyGroup;
    /**
     * 接收人选择的好友列表
     */
    private String agreeGroup;
}
