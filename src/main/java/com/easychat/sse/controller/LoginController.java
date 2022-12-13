package com.easychat.sse.controller;

import com.easychat.sse.dao.MockUserDao;
import com.easychat.sse.model.entity.OldUserEntity;
import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.response.R;
import com.easychat.sse.service.UserService;
import com.easychat.sse.utils.RedisUtil;
import com.wf.captcha.utils.CaptchaUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.easychat.sse.shiro.ShiroUtil.getUser;


@Controller
public class LoginController {

    @Resource
    UserService userService;
    @Resource
    RedisUtil redisUtil;

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
    public R<String> doLogin(@RequestParam("account") String account,
                             @RequestParam("password") String password,
                             @RequestParam("captcha") String captcha,
                             HttpServletRequest request) {
        String storeCaptcha = (String) redisUtil.get(request.getRequestedSessionId());
        if (ObjectUtils.isEmpty(storeCaptcha)) {
            return R.fail("请刷新验证码");
        }
        if (!captcha.equals(storeCaptcha)) {
            redisUtil.del(request.getRequestedSessionId());
            return R.fail("验证码错误");
        }
        userService.login(account, password);
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return R.success();
        }
        return R.fail();
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("username", getUser().getName());
        model.addAttribute("userAvatar", getUser().getAvatarPath());
        return "chat/index";
    }
}
