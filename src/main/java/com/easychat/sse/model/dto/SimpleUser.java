package com.easychat.sse.model.dto;

import com.easychat.sse.utils.MinioUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleUser {
    private String id;
    private String account;
    private String name;
    private String gender;
    private String avatarPath;

    public String getAvatarPath() {
        return MinioUtil.buildPath(avatarPath);
    }
}
