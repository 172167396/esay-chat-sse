package com.easychat.sse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easychat.sse.model.dto.RecentChatDTO;
import com.easychat.sse.model.entity.RecentChat;
import com.easychat.sse.model.vo.RecentChatVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMapper extends BaseMapper<RecentChat> {
    void insertDuplicate(RecentChat entity);

    List<RecentChatDTO> getRecentChat(String userId);

    RecentChat getByUserAndTarget(@Param("userId") String userId,@Param("targetId") String targetId);

    void updateLastActiveTime(String id);
}
