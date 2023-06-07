package com.easychat.sse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easychat.sse.dao.ApplyFriendMapper;
import com.easychat.sse.dao.ChatMapper;
import com.easychat.sse.dao.MessageRecordMapper;
import com.easychat.sse.model.domain.RecentChatDomain;
import com.easychat.sse.model.dto.FriendApplyDTO;
import com.easychat.sse.model.dto.MsgRecordDTO;
import com.easychat.sse.model.dto.RecentChatDTO;
import com.easychat.sse.model.entity.FriendApply;
import com.easychat.sse.model.entity.MsgRecordEntity;
import com.easychat.sse.model.entity.RecentChat;
import com.easychat.sse.model.vo.RecentChatVO;
import com.easychat.sse.service.ChatService;
import com.easychat.sse.utils.IdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.easychat.sse.shiro.ShiroUtil.getUserId;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, RecentChat> implements ChatService {

    @Resource
    ChatMapper chatMapper;
    @Resource
    MessageRecordMapper messageRecordMapper;
    @Resource
    ApplyFriendMapper applyFriendMapper;

    @Override
    public List<RecentChatVO> getRecentChat() {
        String userId = getUserId();
        //查询最近聊天对象
        List<RecentChatDTO> recentChats = chatMapper.getRecentChat(userId);

//        List<MsgRecordDTO> records = messageRecordMapper.getRecord(userId);
        //查最近一条好友申请
        FriendApplyDTO friendApply = applyFriendMapper.getApply(userId);
        return RecentChatDomain.newInstance(recentChats, friendApply).buildVO();
    }

    @Override
    public String dealRecentChat(String userId, String receiver) {
        RecentChat recentChat = getByUserAndTarget(userId, receiver);
        if (recentChat != null) {
            chatMapper.updateLastActiveTime(recentChat.getId());
            return recentChat.getId();
        }
        LocalDateTime now = LocalDateTime.now();
        RecentChat entity = new RecentChat();
        String id = IdUtils.getId();
        entity.setId(id);
        entity.setCreateTime(now);
        entity.setLastActiveTime(now);
        entity.setUserId(userId);
        entity.setTargetId(receiver);
        entity.setType(0);
        chatMapper.insertDuplicate(entity);
        return id;
    }

    @Override
    public RecentChat getByUserAndTarget(String userId, String targetId) {
        return chatMapper.getByUserAndTarget(userId, targetId);
//        if (chat != null) return chat;
//        return chatMapper.getByUserAndTarget(targetId, userId);
    }


}
