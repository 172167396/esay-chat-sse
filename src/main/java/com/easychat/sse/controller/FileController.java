package com.easychat.sse.controller;

import com.easychat.sse.model.domain.UserDomain;
import com.easychat.sse.model.entity.FileEntity;
import com.easychat.sse.response.R;
import com.easychat.sse.response.UploadFileResponse;
import com.easychat.sse.service.FileService;
import com.easychat.sse.service.UserService;
import com.easychat.sse.utils.MinioUtil;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static com.easychat.sse.shiro.ShiroUtil.getUser;
import static com.easychat.sse.utils.MinioUtil.getBase64FromImgUrl;

@RequestMapping("/file")
@RestController
public class FileController {

    @Resource
    FileService fileService;
    @Resource
    UserService userService;

    @PostMapping("/uploadAvatar")
    public R<String> uploadAvatar(@RequestParam MultipartFile file) {
        UploadFileResponse uploadFileResponse = MinioUtil.upload(file);
        String fileId = fileService.saveRecord(uploadFileResponse);
        userService.updateAvatarPath(getUser(), uploadFileResponse.getBucket() + uploadFileResponse.getPath() + uploadFileResponse.getFilename());
        return R.success(fileId,"上传成功");
    }

    @GetMapping("/cropper")
    public ModelAndView cropper() {
        UserDomain user = getUser();
        HashMap<String, String> model = Maps.newHashMap();
        String base64FromImgUrl = getBase64FromImgUrl(user.getAvatarPath());
        model.put("initSrc", base64FromImgUrl);
        return new ModelAndView("chat/cropper", model);
    }

    @GetMapping("/view/{id}")
    public void view(@PathVariable String id, HttpServletResponse response) {
        FileEntity entity = fileService.getById(id);
        if (entity == null) return;
        MinioUtil.writeToResponse(entity.getFileName(), response);
    }
}
