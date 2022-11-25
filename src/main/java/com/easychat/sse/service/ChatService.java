package com.easychat.sse.service;

import com.easychat.sse.model.vo.RecentChatVO;

import java.util.List;

public interface ChatService {
    List<RecentChatVO> getRecentChat();
}
