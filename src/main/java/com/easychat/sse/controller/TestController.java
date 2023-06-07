package com.easychat.sse.controller;

import com.easychat.sse.dao.MessageRecordMapper;
import com.easychat.sse.model.dto.MsgRecordDTO;
import com.easychat.sse.response.R;
import com.easychat.sse.service.MessageRecordService;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.io.IOException;

import static com.easychat.sse.shiro.ShiroUtil.getUserId;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    MessageRecordService messageRecordService;

    @ModelAttribute
    public void entrance(String id, Model model) {
        model.addAttribute("name", id);
    }

    @GetMapping("/getName")
    public String getName(@ModelAttribute(value = "name") String name) {
        return name;
    }

    @GetMapping("/cursor")
    public R<Boolean> testCursor(){
         messageRecordService.listRecord(getUserId());



        return R.success();
    }

}
