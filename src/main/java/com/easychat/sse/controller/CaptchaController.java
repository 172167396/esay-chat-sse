package com.easychat.sse.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.easychat.sse.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Controller
public class CaptchaController {

    @Resource
    RedisUtil redisUtil;

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 90, 4, 100);
        // 验证码值
        String code = lineCaptcha.getCode();
        RedisUtil.set(request.getRequestedSessionId(), code, 1800L);
        try (OutputStream out = response.getOutputStream()) {
            // 输出验证码
            lineCaptcha.write(out);
        } catch (IOException e) {
            try {
                response.sendError(500);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            log.error(e.getMessage(), e);
//            throw new RuntimeException(e);
        }

    }
}
