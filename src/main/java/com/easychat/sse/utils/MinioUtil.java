package com.easychat.sse.utils;

import com.easychat.sse.config.MinioProperties;
import org.springframework.util.ObjectUtils;

public class MinioUtil {

    public static String buildPath(String bucket, String filename) {
        return MinioProperties.getUrl() + "/" + bucket + "/" + filename;
    }

    public static String buildPath(String avatarPath) {
        if (ObjectUtils.isEmpty(avatarPath)) {
            return "";
        }
        return MinioProperties.getUrl() + avatarPath;
    }
}
