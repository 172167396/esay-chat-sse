package com.easychat.sse.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easychat.sse.dao.MessageRecordMapper;
import com.easychat.sse.model.entity.MsgRecordEntity;
import com.easychat.sse.model.entity.RecentChat;
import com.easychat.sse.model.vo.MessageRecordVO;
import com.easychat.sse.service.ChatService;
import com.easychat.sse.service.MessageRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageRecordServiceImpl extends ServiceImpl<MessageRecordMapper, MsgRecordEntity> implements MessageRecordService {
    @Resource
    ChatService chatService;

    @Override
    public List<MessageRecordVO> getRecords(String userId, String targetId) {
        RecentChat recentChat = chatService.getByUserAndTarget(userId, targetId);
        List<MsgRecordEntity> entities = super.list(Wrappers.<MsgRecordEntity>lambdaQuery().eq(MsgRecordEntity::getBelongRecentId, recentChat.getId())
                .orderBy(true, false, MsgRecordEntity::getCreateTime)
                .last(true, "limit 0,30"));
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .sorted(Comparator.comparing(MsgRecordEntity::getCreateTime))
                .map(entity -> MessageRecordVO.from(entity, userId))
                .collect(Collectors.toList());
    }
}
