package com.easychat.sse.controller;

import com.easychat.sse.enums.RecentMsgType;
import com.easychat.sse.model.vo.RecentChatVO;
import com.easychat.sse.response.R;
import com.easychat.sse.service.ChatService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    ChatService chatService;

    @GetMapping("/recent")
    public R<List<RecentChatVO>> recentChat() {
        return R.success(chatService.getRecentChat());
    }

    @GetMapping("/recent/{id}")
    public ModelAndView recentChat(@PathVariable String id) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        String type = RecentMsgType.NEW_FRIEND.name();
        switch (id) {
            case "blank":
                return new ModelAndView("chat/blank");
            case "NEW_FRIEND":
                return new ModelAndView("chat/friendApplies");
        }
        return new ModelAndView("chat/detail", param);
    }


}
