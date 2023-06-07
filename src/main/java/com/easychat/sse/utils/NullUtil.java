package com.easychat.sse.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NullUtil {

    public static String nullAsEmpty(String o) {
        if (o == null) {
            return "";
        }
        return o.trim();
    }

}
