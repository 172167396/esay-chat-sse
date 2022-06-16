package com.easychat.sse.dao;

import com.easychat.sse.model.entity.RequestOfFriend;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyFriendMapper {
    void save(RequestOfFriend entity);
}
