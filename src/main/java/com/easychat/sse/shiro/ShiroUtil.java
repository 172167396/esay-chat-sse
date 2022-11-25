package com.easychat.sse.shiro;

import com.easychat.sse.model.domain.UserDomain;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shiro.SecurityUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShiroUtil {

    public static UserDomain getUser() {
        return (UserDomain) SecurityUtils.getSubject().getPrincipal();
    }

    public static String getUserId() {
        return ((UserDomain) SecurityUtils.getSubject().getPrincipal()).getId();
    }
}
