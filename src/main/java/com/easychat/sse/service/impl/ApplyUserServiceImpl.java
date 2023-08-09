package com.easychat.sse.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easychat.sse.dao.ApplyFriendMapper;
import com.easychat.sse.enums.ApplyState;
import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.model.dto.ApplyAgreeDTO;
import com.easychat.sse.model.dto.FriendApplyDTO;
import com.easychat.sse.model.dto.UserRelationDTO;
import com.easychat.sse.model.entity.FriendApply;
import com.easychat.sse.model.entity.UserRelation;
import com.easychat.sse.model.vo.FriendApplyVO;
import com.easychat.sse.service.ApplyFriendService;
import com.easychat.sse.service.UserRelationService;
import com.easychat.sse.service.UserService;
import com.easychat.sse.utils.ContextHolder;
import com.easychat.sse.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
public class ApplyUserServiceImpl implements ApplyFriendService {

    @Resource
    ApplyFriendMapper applyFriendMapper;
    @Resource
    UserRelationService userRelationService;
    @Resource
    @Lazy
    UserService userService;

    @Override
    public void save(FriendApply entity) {
        applyFriendMapper.save(entity);
    }

    @Override
    public List<FriendApplyVO> queryOwnerReceived(String userId) {
        List<FriendApplyDTO> dtoList = applyFriendMapper.getOwnerApplies(userId);
        if (dtoList.isEmpty()) {
            return Collections.emptyList();
        }
        return FriendApplyVO.fromDtoList(dtoList);
    }

    @Override
    public void agree(String userId, ApplyAgreeDTO args) {
        FriendApply applyEntity = applyFriendMapper.getApplyById(args.getId(), userId);
        if (applyEntity == null) {
            log.debug("申请不存在。已忽略");
            return;
        }
        if (!applyEntity.getReceiveUser().equals(userId)) {
            log.debug("无法同意非添加自己的请求");
            return;
        }
        if (applyEntity.getApplyUser().equals(userId)) {
            log.debug("无法添加自己");
            return;
        }
        if (applyEntity.getState() != ApplyState.NOT_HANDLE.getState()) {
            throw new CustomRuntimeException("该请求已处理,请勿重复操作");
        }
        UserRelation relation = userRelationService.getOne(Wrappers.<UserRelation>lambdaQuery().eq(true, UserRelation::getFriendId, args.getId()));
        if (relation != null) {
            log.debug("ID：{},已经是{}的好友", args.getId(), userId);
        }
        UserRelation userRelation = new UserRelation();
        userRelation.setId(IdUtils.getId());
        userRelation.setUserId(userId);
        userRelation.setFriendId(applyEntity.getApplyUser());
        userRelation.setRemarkName(args.getRemarkName());
        userRelation.setGroupId(args.getGroupId());
        userRelation.setJoinTime(LocalDateTime.now());
        userRelationService.save(userRelation);
        //对方插入一条好友记录
        userRelation.setId(IdUtils.getId());
        userRelation.setUserId(applyEntity.getApplyUser());
        userRelation.setFriendId(userId);
        userRelation.setRemarkName(applyEntity.getNickName());
        String firstGroupId = userService.getFirstGroupId(applyEntity.getApplyUser());
        userRelation.setGroupId(firstGroupId);
        userRelationService.save(userRelation);
        applyFriendMapper.updateApplyState(args.getId(), ApplyState.AGREED.getState());
        UserRelationDTO userRelationDTO = new UserRelationDTO();
        userRelationDTO.setApplyGroup(applyEntity.getGroupId());
        userRelationDTO.setAgreeGroup(args.getGroupId());
        userRelationDTO.setUserId(applyEntity.getApplyUser());
        userRelationDTO.setFriendId(applyEntity.getReceiveUser());
        ContextHolder.publish(userRelationDTO);
    }

    @Override
    public void ignore(String userId, String id) {
        //只更新接收人是自己的记录
        applyFriendMapper.ignoreFriendApply(userId,id);
    }
}
