package com.easychat.sse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easychat.sse.model.entity.RecentChat;
import com.easychat.sse.model.vo.RecentChatVO;

import java.util.List;

public interface ChatService extends IService<RecentChat> {
    List<RecentChatVO> getRecentChat();

    String dealRecentChat(String sender, String receiver);


    RecentChat getByUserAndTarget(String userId,String targetId);
}
