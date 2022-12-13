package com.easychat.sse.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easychat.sse.dao.UserMapper;
import com.easychat.sse.event.RegisterEvent;
import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.model.domain.UserDomain;
import com.easychat.sse.model.dto.*;
import com.easychat.sse.model.entity.FriendApply;
import com.easychat.sse.model.entity.UserEntity;
import com.easychat.sse.model.entity.UserFriendGroup;
import com.easychat.sse.model.entity.UserRelation;
import com.easychat.sse.model.vo.SimpleFriendVO;
import com.easychat.sse.model.vo.SimpleGroupVO;
import com.easychat.sse.service.ApplyFriendService;
import com.easychat.sse.service.UserRelationService;
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
    ApplyFriendService applyFriendService;
    @Resource
    UserRelationService userRelationService;


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
        userEntity.setAvatarPath("/common/awlu4-sgjpy.jpg");
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
        return userMapper.searchUser(content);
    }

    @Override
    public UserEntity getById(String id) {
        return userMapper.getById(id);
    }

    @Override
    public SimpleUser getSimpleUserInfo(String id) {
        return userMapper.getSimpleUserInfo(id);
    }

    @Override
    public List<IdName> queryUserGroup(String userId) {
        return userMapper.queryUserGroup(userId);
    }

    @Override
    public void applyFriend(UserDomain user, ApplyFriendArgs applyFriendArgs) {
        UserEntity targetUser = userMapper.getById(applyFriendArgs.getId());
        if (targetUser == null) {
            throw new CustomRuntimeException("该用户不存在");
        }
        //好友已存在校验
        long count = userRelationService.count(Wrappers.<UserRelation>lambdaQuery()
                .eq(true, UserRelation::getUserId, user.getId())
                .eq(true, UserRelation::getFriendId, targetUser.getId()));
        if (count != 0) {
            throw new CustomRuntimeException("该用户已经是您的好友，无法重复添加");
        }
        if (user.getId().equals(applyFriendArgs.getId())) {
            throw new CustomRuntimeException("无法添加自己");
        }
        FriendApply friendApplyEntity = applyFriendArgs.toEntity(user.getId(), user.getName());
        applyFriendService.save(friendApplyEntity);
        ContextHolder.publish(friendApplyEntity);
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
            String uId = friends.get(0).getId();
            if (uId != null) {
                List<SimpleFriendVO> collect = friends.stream().map(friendDTO -> {
                            SimpleFriendVO simpleFriendVO = new SimpleFriendVO();
                            simpleFriendVO.setId(friendDTO.getUserId());
                            simpleFriendVO.setName(friendDTO.getDisplayName());
                            simpleFriendVO.setAvatar(MinioUtil.buildPath(friendDTO.getAvatarPath()));
                            return simpleFriendVO;
                        }).sorted(Comparator.comparing(SimpleFriendVO::getName, Comparator.nullsLast(Comparator.naturalOrder())))
                        .collect(Collectors.toList());
                vo.setUsers(collect);
            }
            voList.add(vo);
        });
        return voList;
    }

    @Override
    public String getFirstGroupId(String applyUser) {
        return userMapper.getFirstGroupId(applyUser);
    }


}
