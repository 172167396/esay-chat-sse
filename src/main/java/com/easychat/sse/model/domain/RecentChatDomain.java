package com.easychat.sse.model.domain;

import com.easychat.sse.config.MinioProperties;
import com.easychat.sse.enums.MessageType;
import com.easychat.sse.enums.RecentMsgType;
import com.easychat.sse.model.dto.FriendApplyDTO;
import com.easychat.sse.model.dto.MsgRecordDTO;
import com.easychat.sse.model.dto.RecentChatDTO;
import com.easychat.sse.model.vo.RecentChatVO;
import com.easychat.sse.utils.MinioUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecentChatDomain {
    private List<RecentChatDTO> recentChatDTOS;
    private FriendApplyDTO friendApply;


    public static RecentChatDomain newInstance(List<RecentChatDTO> recentChatDTOS,
                                               FriendApplyDTO friendApply) {
        RecentChatDomain recentChatDomain = new RecentChatDomain();
        recentChatDomain.friendApply = friendApply;
        recentChatDomain.recentChatDTOS = recentChatDTOS;
        return recentChatDomain;
    }

    public List<RecentChatVO> buildVO() {
        if (CollectionUtils.isEmpty(recentChatDTOS) && friendApply == null) {
            return Collections.emptyList();
        }
        List<RecentChatVO> recentChatVOS = recentChatDTOS.stream().map(record -> new RecentChatVO(record.getId(),
                        record.getRemarkName(),
                        record.getBriefMsg(),
                        RecentMsgType.PERSONAL,
                        record.getLastActiveTime(),
                        record.getLastActiveTime(),
                        record.getTargetId(),
                        MinioUtil.buildPath(record.getAvatarPath())))
                .collect(Collectors.toList());
        if (friendApply != null) {
            RecentChatVO recentChatVO = new RecentChatVO(friendApply.getId(),
                    "新朋友",
                    friendApply.getUserName() + "申请添加你为好友",
                    RecentMsgType.NEW_FRIEND,
                    friendApply.getCreateTime(),
                    friendApply.getCreateTime(),
                    RecentMsgType.NEW_FRIEND.name(),
                    null);
            recentChatVOS.add(recentChatVO);
        }
        return recentChatVOS.stream()
                .sorted(Comparator.comparing(RecentChatVO::getLastActiveTime, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
    }
}
