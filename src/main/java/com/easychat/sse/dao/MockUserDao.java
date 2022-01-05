package com.easychat.sse.dao;

import com.easychat.sse.entity.UserEntity;
import com.easychat.sse.exception.CustomRuntimeException;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MockUserDao {

    private static final List<String> avatars = Arrays.asList("avatar1.jpg", "avatar2.jpg", "avatar3.jpg", "avatar4.jpg");

    public static final Map<String, UserEntity> userMap = new ConcurrentHashMap<>();

    public static UserEntity require(String id) {
        UserEntity userEntity = userMap.get(id);
        if (userEntity == null) {
            throw new CustomRuntimeException("用户不存在");
        }
        return userEntity;
    }

    public static UserEntity requireThrow(String id,String msg) {
        UserEntity userEntity = userMap.get(id);
        if (userEntity == null) {
            throw new CustomRuntimeException(msg);
        }
        return userEntity;
    }


    public static UserEntity registryLogin(String userName) {
        boolean sameName = userMap.values().stream().anyMatch(u -> u.getUserName().equals(userName));
        if (sameName) throw new CustomRuntimeException("用户名已存在");
        String userId = UUID.randomUUID().toString().replace("-", "");
        int avatar = (int) Math.floor(Math.random() * 4);
        String avatarName = avatars.get(avatar);
        UserEntity user = new UserEntity(userId, userName, avatarName, LocalTime.now(),null);
        userMap.put(userId, user);
        return user;
    }
}
