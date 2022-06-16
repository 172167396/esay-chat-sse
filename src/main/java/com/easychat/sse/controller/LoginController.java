package com.easychat.sse.controller;

import com.easychat.sse.dao.MockUserDao;
import com.easychat.sse.model.entity.OldUserEntity;
import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.response.R;
import com.easychat.sse.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.easychat.sse.shiro.ShiroUtil.getUser;


@Controller
public class LoginController {

    @Resource
    UserService userService;

    @GetMapping({"", "/"})
    public String redirect() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "chat/login";
    }

    @GetMapping("/register")
    public String register() {
        return "chat/register";
    }


    @PostMapping("/login")
    @ResponseBody
    public R<String> doLogin(@RequestParam("account") String account, @RequestParam("password") String password) {
        userService.login(account, password);
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return R.success();
        }
        return R.fail();
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("username", getUser().getName());
        return "chat/index";
    }
}
