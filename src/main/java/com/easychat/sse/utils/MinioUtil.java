package com.easychat.sse.utils;

import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.easychat.sse.config.MinioProperties;
import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.response.UploadFileResponse;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class MinioUtil {

    private static final String avatarPath = "/avatar/";
    private static final String commonBucket = "common";

    @Resource
    private MinioClient minioClient;

    private static MinioUtil instance;

    @PostConstruct
    public void init() {
        instance = this;
    }

    public static String buildPath(String bucket, String filename) {
        return MinioProperties.getUrl() + "/" + bucket + "/" + filename;
    }

    public static String buildPath(String avatarPath) {
        if (ObjectUtils.isEmpty(avatarPath)) {
            return "";
        }
        return MinioProperties.getUrl() + (avatarPath.startsWith("/") ? avatarPath : "/" + avatarPath);
    }

    public static UploadFileResponse upload(MultipartFile file) {
        String originName = file.getOriginalFilename();
        if (originName == null) {
            throw new CustomRuntimeException("文件名不能为空");
        }
        String fileName = IdUtils.getId() + "." + FileNameUtil.extName(originName);
        try (InputStream in = file.getInputStream()) {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(commonBucket)
                    .object(avatarPath + fileName)
                    .contentType(file.getContentType())
                    .stream(in, file.getSize(), -1).build();
            instance.minioClient.putObject(args);
            return new UploadFileResponse(commonBucket, fileName, avatarPath);
        } catch (Exception e) {
            throw new CustomRuntimeException(e);
        }
    }

    public static void writeToResponse(String filename, HttpServletResponse response) {
        StatObjectArgs statObjectArgs = StatObjectArgs.builder().bucket(commonBucket).object(filename).build();

        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(commonBucket)
                .object(filename)
                .build();
        try (GetObjectResponse res = instance.minioClient.getObject(args);
             OutputStream out = response.getOutputStream()) {
//            StatObjectResponse statObjectResponse = instance.minioClient.statObject(statObjectArgs);
//            log.debug(statObjectResponse.toString());
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "inline;filename=" + filename);
            StreamUtils.copy(res, out);
        } catch (Exception e) {
            throw new CustomRuntimeException(e);
        }
    }

    public static String getBase64FromImgUrl(String url) {
        if (StringUtils.isNotBlank(url)) {
            try {
                byte[] bytes = IOUtils.toByteArray(new URL(url));
                return "data:image/gif;base64," + Base64.getEncoder().encodeToString(bytes);
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
}
