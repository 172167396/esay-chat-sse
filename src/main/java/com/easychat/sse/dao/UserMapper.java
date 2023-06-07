package com.easychat.sse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easychat.sse.model.domain.UserDomain;
import com.easychat.sse.model.dto.IdName;
import com.easychat.sse.model.dto.IdTitle;
import com.easychat.sse.model.dto.SimpleFriendDTO;
import com.easychat.sse.model.dto.SimpleUser;
import com.easychat.sse.model.entity.UserEntity;
import com.easychat.sse.model.entity.UserFriendGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<UserEntity> {

    UserEntity getUserByAccount(String userAccount);

    void save(UserEntity userEntity);

    List<IdTitle> getUserGroups(String userId);

    void initUserGroup(UserFriendGroup userFriendFroup);

    List<SimpleUser> searchUser(String content);

    UserEntity getById(String id);

    SimpleUser getSimpleUserInfo(String id);

    List<IdName> queryUserGroup(String userId);

    UserDomain getUserDomainByAccount(String username);

    List<SimpleFriendDTO> findMyFriends(@Param("userId") String userId,@Param("groupId") String groupId);

    String getFirstGroupId(String applyUser);

    void updateAvatarPath(@Param("userId") String userId,@Param("avatarPath") String avatarPath);

}
