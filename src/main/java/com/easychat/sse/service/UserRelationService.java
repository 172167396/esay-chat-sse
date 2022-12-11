package com.easychat.sse.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.easychat.sse.model.dto.SimpleFriendDTO;
import com.easychat.sse.model.entity.UserRelation;

public interface UserRelationService extends IService<UserRelation> {
    SimpleFriendDTO validateUserRelation(String sender, String targetUser);

}
