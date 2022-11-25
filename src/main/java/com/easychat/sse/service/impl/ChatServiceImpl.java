package com.easychat.sse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.easychat.sse.dao.ApplyFriendMapper;
import com.easychat.sse.dao.ChatMapper;
import com.easychat.sse.dao.MessageRecordMapper;
import com.easychat.sse.model.domain.RecentChatDomain;
import com.easychat.sse.model.dto.FriendApplyDTO;
import com.easychat.sse.model.dto.MsgRecordDTO;
import com.easychat.sse.model.entity.FriendApply;
import com.easychat.sse.model.entity.MsgRecordEntity;
import com.easychat.sse.model.vo.RecentChatVO;
import com.easychat.sse.service.ChatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.easychat.sse.shiro.ShiroUtil.getUserId;

@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    ChatMapper chatMapper;
    @Resource
    MessageRecordMapper messageRecordMapper;
    @Resource
    ApplyFriendMapper applyFriendMapper;

    @Override
    public List<RecentChatVO> getRecentChat() {
        String userId = getUserId();
        //查询最近聊天记录
        List<MsgRecordDTO> records = messageRecordMapper.getRecord(userId);
        //查最近一条好友申请
        FriendApplyDTO friendApply = applyFriendMapper.getApply(userId);
        return RecentChatDomain.newInstance(records, friendApply).buildVO();
    }
}
