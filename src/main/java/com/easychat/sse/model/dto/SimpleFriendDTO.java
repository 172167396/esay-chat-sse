package com.easychat.sse.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

@Getter
@Setter
public class SimpleFriendDTO extends IdName {
    private String bucket;
    private String fileName;
    private String userId;
    /**
     * 备注，为空时显示原本的名字
     */
    private String remarkName;
    private String groupId;
    private String groupName;

    public String getDisplayName() {
        if (!ObjectUtils.isEmpty(remarkName)) {
            return remarkName;
        }
        return getName();
    }
}
