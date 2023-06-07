package com.easychat.sse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easychat.sse.dao.MessageRecordMapper;
import com.easychat.sse.model.dto.MsgRecordDTO;
import com.easychat.sse.model.entity.MsgRecordEntity;
import com.easychat.sse.model.vo.MessageRecordVO;
import com.easychat.sse.service.MessageRecordService;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageRecordServiceImpl extends ServiceImpl<MessageRecordMapper, MsgRecordEntity> implements MessageRecordService {

    @Resource
    MessageRecordMapper messageRecordMapper;

    @Override
    public List<MessageRecordVO> getRecords(String userId, String targetId) {
        List<MsgRecordEntity> entities = messageRecordMapper.getRecordByUserIdAndTargetId(userId, targetId);
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .sorted(Comparator.comparing(MsgRecordEntity::getCreateTime))
                .map(entity -> MessageRecordVO.from(entity, userId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void listRecord(String userId) {
        try (Cursor<MsgRecordDTO> recordDTOS = messageRecordMapper.listRecord(userId)) {
            recordDTOS.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
