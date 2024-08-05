package com.easychat.sse.controller;

import com.easychat.sse.utils.RedisUtil;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Controller
public class CaptchaController {


    @GetMapping("/captcha")
    @ResponseBody
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setLen(2);
        captcha.getArithmeticString();
        String text = captcha.text();
        RedisUtil.set(request.getSession().getId(), text, 1800L);
        response.setContentType("image/png");
        try (OutputStream out = response.getOutputStream()) {
            captcha.out(out);
            log.info("验证码为:{}", text);
        } catch (IOException e) {
            try {
                response.sendError(500);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            log.error(e.getMessage(), e);
        }

    }
}
