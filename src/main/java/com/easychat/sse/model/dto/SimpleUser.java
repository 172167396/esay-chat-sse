package com.easychat.sse.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

@Getter
@Setter
public class SimpleUser {
    private String id;
    private String account;
    private String name;
    private String gender;
    private String avatarUrl;
    @JsonIgnore
    private String bucket;
    @JsonIgnore
    private String fileName;
    @JsonIgnore
    private String minioEndPoint;

    public String getAvatarUrl() {
        if (!ObjectUtils.isEmpty(avatarUrl)) {
            return avatarUrl;
        }
        return minioEndPoint + "/" + bucket + "/" + fileName;
    }
}
