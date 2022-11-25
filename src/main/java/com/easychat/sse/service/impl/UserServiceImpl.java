package com.easychat.sse.service.impl;

import com.easychat.sse.config.MinioProperties;
import com.easychat.sse.dao.UserMapper;
import com.easychat.sse.event.RegisterEvent;
import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.model.domain.UserDomain;
import com.easychat.sse.model.dto.*;
import com.easychat.sse.model.entity.UserEntity;
import com.easychat.sse.model.entity.UserFriendGroup;
import com.easychat.sse.model.vo.SimpleFriendVO;
import com.easychat.sse.model.vo.SimpleGroupVO;
import com.easychat.sse.service.ApplyFriendService;
import com.easychat.sse.service.UserService;
import com.easychat.sse.utils.ContextHolder;
import com.easychat.sse.utils.IdUtils;
import com.easychat.sse.utils.MD5Utils;
import com.easychat.sse.utils.MinioUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.easychat.sse.constant.Constant.DEFAULT_GROUP;
import static com.easychat.sse.constant.Constant.PASSWD_SECRET;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    MinioProperties minioProperties;
    @Resource
    ApplyFriendService applyFriendService;

    @Override
    public UserEntity getUserByAccount(String userAccount) {
        return userMapper.getUserByAccount(userAccount);
    }

    @Override
    public void login(String account, String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(account, MD5Utils.getMd5(PASSWD_SECRET + password), false);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException e) {
            throw new CustomRuntimeException("账号或密码不正确");
        } catch (Exception e) {
            throw new CustomRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void doRegister(String account, String nickName, String password) {
        if (ObjectUtils.isEmpty(account)
                || ObjectUtils.isEmpty(nickName)
                || ObjectUtils.isEmpty(password)) {
            throw new CustomRuntimeException("请完善信息");
        }
        UserEntity user = userMapper.getUserByAccount(account);
        if (user != null) {
            throw new CustomRuntimeException("账户名已存在！");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(IdUtils.getId());
        userEntity.setAccount(account);
        userEntity.setName(nickName);
        userEntity.setPassword(MD5Utils.getMd5(PASSWD_SECRET + password));
        userEntity.setGender("1");
        userMapper.save(userEntity);
        ContextHolder.publish(new RegisterEvent(userEntity.getId()));
    }

    @Override
    public List<IdTitle> getUserGroups(String userId) {
        return userMapper.getUserGroups(userId);
    }

    @Override
    public void initUserGroup(String userId) {
        UserFriendGroup friendGroup = new UserFriendGroup();
        friendGroup.setId(IdUtils.getId());
        friendGroup.setName(DEFAULT_GROUP);
        friendGroup.setCreateUser(userId);
        friendGroup.setCreateTime(LocalDateTime.now());
        userMapper.initUserGroup(friendGroup);
    }

    @Override
    public List<SimpleUser> searchUser(String content) {
        List<SimpleUser> simpleUsers = userMapper.searchUser(content);
        simpleUsers.forEach(user -> user.setMinioEndPoint(minioProperties.getEndPoint()));
        return simpleUsers;
    }

    @Override
    public UserEntity getById(String id) {
        return userMapper.getById(id);
    }

    @Override
    public SimpleUser getSimpleUserInfo(String id) {
        SimpleUser simpleUserInfo = userMapper.getSimpleUserInfo(id);
        simpleUserInfo.setMinioEndPoint(minioProperties.getEndPoint());
        return simpleUserInfo;
    }

    @Override
    public List<IdName> queryUserGroup(String userId) {
        return userMapper.queryUserGroup(userId);
    }

    @Override
    public void applyFriend(UserEntity user, ApplyFriendArgs applyFriendArgs) {
        UserEntity targetUser = userMapper.getById(applyFriendArgs.getId());
        if (targetUser == null) {
            throw new CustomRuntimeException("该用户不存在");
        }
        //
        if (user.getId().equals(applyFriendArgs.getId())) {
            throw new CustomRuntimeException("无法添加自己");
        }
        applyFriendService.save(applyFriendArgs.toEntity(user.getId()));

    }

    @Override
    public UserDomain getUserDomainByAccount(String username) {
        return userMapper.getUserDomainByAccount(username);
    }


    @Override
    public List<SimpleGroupVO> findMyFriends(String userId, String groupId) {
        List<SimpleFriendDTO> friendDTOS = userMapper.findMyFriends(userId, groupId);
        List<SimpleGroupVO> voList = new ArrayList<>();
        if (friendDTOS.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, List<SimpleFriendDTO>> groupMap = friendDTOS.stream()
                .collect(Collectors.groupingBy(SimpleFriendDTO::getGroupId));
        groupMap.forEach((id, friends) -> {
            SimpleGroupVO vo = new SimpleGroupVO();
            vo.setId(id);
            vo.setGroupName(friends.get(0).getGroupName());
            List<SimpleFriendVO> collect = friends.stream().map(friendDTO -> {
                        SimpleFriendVO simpleFriendVO = new SimpleFriendVO();
                        simpleFriendVO.setId(friendDTO.getUserId());
                        simpleFriendVO.setName(friendDTO.getDisplayName());
                        simpleFriendVO.setAvatar(MinioUtil.buildPath(friendDTO.getBucket(), friendDTO.getFileName()));
                        return simpleFriendVO;
                    }).sorted(Comparator.comparing(SimpleFriendVO::getName, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
            vo.setUsers(collect);
            voList.add(vo);
        });
        return voList;
    }


}
