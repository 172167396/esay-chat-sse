package com.easychat.sse.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SimpleGroupVO {
    /**
     * groupId
     */
    private String id;
    private String groupName;
    private List<SimpleFriendVO> users;
}
