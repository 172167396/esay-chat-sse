package com.easychat.sse.dao;

import com.easychat.sse.model.entity.OldUserEntity;
import com.easychat.sse.exception.CustomRuntimeException;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MockUserDao {

    private static final List<String> avatars = Arrays.asList("avatar1.jpg", "avatar2.jpg", "avatar3.jpg", "avatar4.jpg");

    public static final Map<String, OldUserEntity> userMap = new ConcurrentHashMap<>();

    public static OldUserEntity require(String id) {
        OldUserEntity oldUserEntity = userMap.get(id);
        if (oldUserEntity == null) {
            throw new CustomRuntimeException("用户不存在");
        }
        return oldUserEntity;
    }

    public static OldUserEntity requireThrow(String id, String msg) {
        OldUserEntity oldUserEntity = userMap.get(id);
        if (oldUserEntity == null) {
            throw new CustomRuntimeException(msg);
        }
        return oldUserEntity;
    }


    public static OldUserEntity registryLogin(String account) {
        boolean sameName = userMap.values().stream().anyMatch(u -> u.getAccount().equals(account));
        if (sameName) throw new CustomRuntimeException("用户名已存在");
        String userId = UUID.randomUUID().toString().replace("-", "");
        int avatar = (int) Math.floor(Math.random() * 4);
        String avatarName = avatars.get(avatar);
        OldUserEntity user = new OldUserEntity(userId, account, avatarName, LocalTime.now(),null);
        userMap.put(userId, user);
        return user;
    }
}
