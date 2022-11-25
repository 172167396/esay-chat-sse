package com.easychat.sse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easychat.sse.model.dto.MsgRecordDTO;
import com.easychat.sse.model.entity.MsgRecordEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRecordMapper extends BaseMapper<MsgRecordEntity> {

    List<MsgRecordDTO> getRecord(String userId);

}
