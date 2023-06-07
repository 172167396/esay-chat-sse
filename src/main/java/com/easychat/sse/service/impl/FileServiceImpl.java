package com.easychat.sse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easychat.sse.dao.FileMapper;
import com.easychat.sse.model.entity.FileEntity;
import com.easychat.sse.response.UploadFileResponse;
import com.easychat.sse.service.FileService;
import com.easychat.sse.utils.IdUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.easychat.sse.shiro.ShiroUtil.getUser;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileEntity> implements FileService {

    @Override
    public String saveRecord(UploadFileResponse uploadFileResponse) {
        FileEntity entity = new FileEntity();
        String id = IdUtils.getId();
        entity.setId(id);
        entity.setBucket(uploadFileResponse.getBucket());
        entity.setFileName(uploadFileResponse.getPath() + uploadFileResponse.getFilename());
        entity.setType(0);
        entity.setCreateUser(getUser().getName());
        entity.setCreateTime(LocalDateTime.now());
        save(entity);
        return id;
    }
}
