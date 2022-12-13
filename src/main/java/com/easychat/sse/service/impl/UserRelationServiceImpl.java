package com.easychat.sse.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easychat.sse.dao.UserRelationMapper;
import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.model.dto.SimpleFriendDTO;
import com.easychat.sse.model.entity.UserRelation;
import com.easychat.sse.service.UserRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.easychat.sse.shiro.ShiroUtil.getUserId;

@Service
public class UserRelationServiceImpl extends ServiceImpl<UserRelationMapper, UserRelation> implements UserRelationService {

    @Resource
    UserRelationMapper userRelationMapper;

    /**
     * 校验好友关系，返回sender的头像，以及sender在targetUser里的备注
     */
    @Override
    public SimpleFriendDTO validateUserRelation(String sender, String targetUser) {
//        long count = count(Wrappers.<UserRelation>lambdaQuery().eq(UserRelation::getUserId, sender).eq(UserRelation::getFriendId, targetUser));
        SimpleFriendDTO simpleFriendDTO = userRelationMapper.findSimpleFriend(sender, targetUser);
        if (simpleFriendDTO == null) {
            throw new CustomRuntimeException("请先添加好友");
        }
        return simpleFriendDTO;
    }
}
