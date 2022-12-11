package com.easychat.sse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easychat.sse.model.dto.SimpleFriendDTO;
import com.easychat.sse.model.entity.UserRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRelationMapper extends BaseMapper<UserRelation> {

    SimpleFriendDTO findSimpleFriend(@Param("userId") String sender,@Param("targetId") String targetUser);

}
