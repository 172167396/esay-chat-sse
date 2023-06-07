package com.easychat.sse.response;

import com.easychat.sse.utils.MinioUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
public class UploadFileResponse {

    String bucket;
    String filename;
    String path;

    public String completePath() {
        return MinioUtil.buildPath(bucket + path + filename);
    }
}
