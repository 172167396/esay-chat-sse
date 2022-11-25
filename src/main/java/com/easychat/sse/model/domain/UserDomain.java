package com.easychat.sse.model.domain;

import com.easychat.sse.model.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDomain extends UserEntity implements Serializable {
    private String endPoint;
    private String bucket;
    private String fileName;

    public String getAvatarUrl() {
        return endPoint + "/" + bucket + "/" + fileName;
    }
}
