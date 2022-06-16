package com.easychat.sse.event.handler;

import com.easychat.sse.event.RegisterEvent;
import com.easychat.sse.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RegisterEventHandler {

    @Resource
    UserService userService;

    @EventListener(RegisterEvent.class)
    public void afterRegister(RegisterEvent event){
        userService.initUserGroup(event.getUserId());
    }
}
