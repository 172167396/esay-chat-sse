package com.easychat.sse.service.impl;

import com.easychat.sse.dao.ApplyFriendMapper;
import com.easychat.sse.model.entity.RequestOfFriend;
import com.easychat.sse.service.ApplyFriendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ApplyUserServiceImpl implements ApplyFriendService {

    @Resource
    ApplyFriendMapper applyFriendMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RequestOfFriend entity) {
        applyFriendMapper.save(entity);
    }
}
