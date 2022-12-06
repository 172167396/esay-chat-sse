package com.easychat.sse.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easychat.sse.dao.UserRelationMapper;
import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.model.entity.UserRelation;
import com.easychat.sse.service.UserRelationService;
import org.springframework.stereotype.Service;

import static com.easychat.sse.shiro.ShiroUtil.getUserId;

@Service
public class UserRelationServiceImpl extends ServiceImpl<UserRelationMapper, UserRelation> implements UserRelationService {


    @Override
    public void validateUserRelation(String targetUser) {
        long count = count(Wrappers.<UserRelation>lambdaQuery().eq(UserRelation::getUserId, getUserId()).eq(UserRelation::getFriendId, targetUser));
        if (count == 0) {
            throw new CustomRuntimeException("请先添加好友");
        }
    }
}
