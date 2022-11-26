package com.easychat.sse.model.dto;

import com.easychat.sse.enums.ApplyState;
import com.easychat.sse.model.entity.FriendApply;
import com.easychat.sse.utils.IdUtils;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class ApplyFriendArgs {
    @NotBlank
    private String id;
    @NotBlank
    private String groupId;
    private String nickName;
    private String remark;


    public FriendApply toEntity(String applyUserId){
        FriendApply entity = new FriendApply();
        entity.setId(IdUtils.getId());
        entity.setApplyUser(applyUserId);
        entity.setRemark(remark);
        entity.setReceiveUser(id);
        entity.setGroupId(groupId);
        entity.setState(ApplyState.NOT_HANDLE.getState());
        entity.setCreateTime(LocalDateTime.now());
        return entity;
    }
}
