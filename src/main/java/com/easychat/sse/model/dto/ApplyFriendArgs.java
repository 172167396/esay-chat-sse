package com.easychat.sse.model.dto;

import com.easychat.sse.enums.ApplyState;
import com.easychat.sse.model.entity.RequestOfFriend;
import com.easychat.sse.utils.IdUtils;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ApplyFriendArgs {
    @NotBlank
    private String id;
    @NotBlank
    private String groupId;
    private String nickName;
    private String remark;


    public RequestOfFriend toEntity(String applyUserId){
        RequestOfFriend entity = new RequestOfFriend();
        entity.setId(IdUtils.getId());
        entity.setApplyUser(applyUserId);
        entity.setRemark(remark);
        entity.setReceiveUser(id);
        entity.setState(ApplyState.NOT_HANDLE.getState());
        return entity;
    }
}
