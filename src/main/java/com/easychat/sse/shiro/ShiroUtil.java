package com.easychat.sse.shiro;

import com.easychat.sse.model.entity.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shiro.SecurityUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShiroUtil {

    public static UserEntity getUser() {
        return (UserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    public static String getUserId() {
        return ((UserEntity) SecurityUtils.getSubject().getPrincipal()).getId();
    }
}
