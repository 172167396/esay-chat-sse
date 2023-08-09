package com.easychat.sse.controller;

import com.easychat.sse.dao.MessageRecordMapper;
import com.easychat.sse.model.dto.MsgRecordDTO;
import com.easychat.sse.model.entity.UserEntity;
import com.easychat.sse.response.R;
import com.easychat.sse.service.MessageRecordService;
import com.easychat.sse.service.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.easychat.sse.shiro.ShiroUtil.getUserId;

@RestController
public class TestController {

    @Resource
    MessageRecordService messageRecordService;
    @Resource
    UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private int i = 0;

    @GetMapping("/getName")
    public String getName(@ModelAttribute(value = "name") String name) {
        return name;
    }

    @GetMapping("/cursor")
    public R<Boolean> testCursor() {
        messageRecordService.listRecord(getUserId());
        return R.success();
    }

    @GetMapping("/getRouters")
    public R<Router[]> getRouters() {
        try (InputStream in = Files.newInputStream(Paths.get("src/main/java/com/easychat/sse/controller/simpleRouters.json"))) {
            Router[] routers = objectMapper.readValue(in, Router[].class);
            return R.success(routers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/users")
    public R<List<UserEntity>> getUsers() {
        return R.success(userService.list());
    }


    @Getter
    @Setter
    static class Router {
        private String name;
        private String path;
        private String component;
        private Meta meta;
        private List<Router> children;
    }

    @Getter
    @Setter
    static class Meta {
        private String title;
        private String icon;
        private String link;
    }
}
