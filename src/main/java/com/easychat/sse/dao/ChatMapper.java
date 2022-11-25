package com.easychat.sse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easychat.sse.model.entity.RecentChat;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMapper extends BaseMapper<RecentChat> {
}
