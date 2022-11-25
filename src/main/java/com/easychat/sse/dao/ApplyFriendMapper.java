package com.easychat.sse.dao;

import com.easychat.sse.model.dto.FriendApplyDTO;
import com.easychat.sse.model.entity.FriendApply;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyFriendMapper {
    void save(FriendApply entity);

    FriendApplyDTO getApply(String userId);

    List<FriendApplyDTO> getOwnerApplies(String userId);

    FriendApply getApplyById(@Param("id") String id,@Param("receiveUser") String receiveUser);

    void updateApplyState(@Param("id") String id, @Param("state") int state);
}
