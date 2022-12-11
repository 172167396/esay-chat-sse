package com.easychat.sse.utils;

import com.easychat.sse.config.MinioProperties;

public class MinioUtil {

    public static String buildPath(String bucket, String filename) {
        return MinioProperties.getUrl() + "/" + bucket + "/" + filename;
    }

    public static String buildPath(String avatarPath) {
        return MinioProperties.getUrl() + avatarPath;
    }
}
