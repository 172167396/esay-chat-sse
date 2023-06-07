package com.easychat.sse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easychat.sse.model.entity.FileEntity;
import com.easychat.sse.response.UploadFileResponse;

public interface FileService extends IService<FileEntity> {
    String saveRecord(UploadFileResponse uploadFileResponse);

}
