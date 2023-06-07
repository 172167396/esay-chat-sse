package com.easychat.sse.controller;

import com.easychat.sse.utils.RedisUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
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
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(126, 35);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的公式：4-9+1=?
        captcha.getArithmeticString();
        // 获取运算的结果：-4
        String text = captcha.text();
        RedisUtil.set(request.getRequestedSessionId(), text, 1800L);
        try (OutputStream out = response.getOutputStream()) {
            // 输出验证码
            captcha.out(out);
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
