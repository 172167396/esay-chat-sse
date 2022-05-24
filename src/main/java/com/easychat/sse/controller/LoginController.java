package com.easychat.sse.controller;

import com.easychat.sse.dao.MockUserDao;
import com.easychat.sse.entity.UserEntity;
import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.response.R;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LoginController {

    @GetMapping({"", "/"})
    public String redirect() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "chat/login";
    }

    @GetMapping("/doLogin")
    @ResponseBody
    public R<String> doLogin(String userName) {
        String id = MockUserDao.registryLogin(userName).getId();
        return R.success(id);
    }

    @GetMapping("/index")
    public String index(Model model, @RequestParam String userId) {
        UserEntity userEntity = MockUserDao.userMap.get(userId);
        if (userEntity == null) {
            throw new CustomRuntimeException("用户不存在");
        }
        model.addAttribute("userId", userId);
        return "chat/index";
    }
}
