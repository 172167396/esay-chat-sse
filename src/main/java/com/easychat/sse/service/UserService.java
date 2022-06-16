package com.easychat.sse.service;

import com.easychat.sse.model.dto.ApplyFriendArgs;
import com.easychat.sse.model.dto.IdName;
import com.easychat.sse.model.dto.IdTitle;
import com.easychat.sse.model.dto.SimpleUser;
import com.easychat.sse.model.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity getUserByAccount(String userAccount);
    void login(String account, String password);
    void doRegister(String account, String nickName, String password);
    List<IdTitle> getUserGroups(String userId);
    void initUserGroup(String userId);

    List<SimpleUser> searchUser(String content);

    UserEntity getById(String id);

    SimpleUser getSimpleUserInfo(String id);

    List<IdName> queryUserGroup(String userId);

    void applyFriend(UserEntity user, ApplyFriendArgs applyFriendArgs);

}
