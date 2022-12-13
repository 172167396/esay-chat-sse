package com.easychat.sse.controller;

import com.easychat.sse.enums.RecentMsgType;
import com.easychat.sse.model.vo.MessageRecordVO;
import com.easychat.sse.model.vo.RecentChatVO;
import com.easychat.sse.response.R;
import com.easychat.sse.service.ChatService;
import com.easychat.sse.service.MessageRecordService;
import com.easychat.sse.utils.DateTimeFormatUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.easychat.sse.shiro.ShiroUtil.getUserId;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    ChatService chatService;
    @Resource
    MessageRecordService messageRecordService;

    @GetMapping("/recent")
    public R<List<RecentChatVO>> recentChat() {
        return R.success(chatService.getRecentChat());
    }

    @GetMapping("/recent/{id}")
    public ModelAndView recentChat(@PathVariable String id) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        param.put("createTime", LocalDateTime.now().format(DateTimeFormatUtil.YYYY_MM_DD));
        String type = RecentMsgType.NEW_FRIEND.name();
        switch (id) {
            case "blank":
                return new ModelAndView("chat/blank");
            case "NEW_FRIEND":
                return new ModelAndView("chat/friendApplies");
        }
        chatService.dealRecentChat(getUserId(), id);
        return new ModelAndView("chat/detail", param);
    }

    @GetMapping("/records/{id}")
    public R<List<MessageRecordVO>> getRecords(@PathVariable String id) {
        List<MessageRecordVO> records = messageRecordService.getRecords(getUserId(), id);
        return R.success(records);
    }

}
