package com.easychat.sse.service;

import com.easychat.sse.model.dto.ApplyAgreeDTO;
import com.easychat.sse.model.entity.FriendApply;
import com.easychat.sse.model.vo.FriendApplyVO;

import java.util.List;

public interface ApplyFriendService {
    void save(FriendApply toEntity);

    List<FriendApplyVO> queryOwnerReceived(String userId);

    void agree(String userId, ApplyAgreeDTO args);

}
