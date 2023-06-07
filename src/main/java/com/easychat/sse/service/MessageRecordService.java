package com.easychat.sse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easychat.sse.model.dto.MsgRecordDTO;
import com.easychat.sse.model.entity.MsgRecordEntity;
import com.easychat.sse.model.vo.MessageRecordVO;
import org.apache.ibatis.cursor.Cursor;

import java.util.List;

public interface MessageRecordService extends IService<MsgRecordEntity> {
    List<MessageRecordVO> getRecords(String userId, String id);

    void listRecord(String userId);

}
