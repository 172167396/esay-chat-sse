package com.easychat.sse.model.domain;

import com.easychat.sse.config.MinioProperties;
import com.easychat.sse.enums.RecentMsgType;
import com.easychat.sse.model.dto.FriendApplyDTO;
import com.easychat.sse.model.dto.MsgRecordDTO;
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
    private List<MsgRecordDTO> recordEntities;
    private FriendApplyDTO friendApply;


    public static RecentChatDomain newInstance(List<MsgRecordDTO> recordEntities,
                                               FriendApplyDTO friendApply) {
        RecentChatDomain recentChatDomain = new RecentChatDomain();
        recentChatDomain.friendApply = friendApply;
        recentChatDomain.recordEntities = recordEntities;
        return recentChatDomain;
    }

    public List<RecentChatVO> buildVO() {
        if (CollectionUtils.isEmpty(recordEntities) && friendApply == null) {
            return Collections.emptyList();
        }
        List<RecentChatVO> recentChatVOS = recordEntities.stream().map(record -> new RecentChatVO(record.getId(),
                        record.getUserName(),
                        record.getContent(),
                        RecentMsgType.PERSONAL,
                        record.getCreateTime(),
                        record.getCreateTime(),
                        record.getReceiverId(),
                        MinioUtil.buildPath(record.getBucket(), record.getFileName())))
                .collect(Collectors.toList());
        if (friendApply != null) {
            RecentChatVO recentChatVO = new RecentChatVO(friendApply.getId(),
                    "新朋友",
                    friendApply.getUserName() + "申请添加你为好友",
                    RecentMsgType.NEW_FRIEND,
                    friendApply.getCreateTime(),
                    friendApply.getCreateTime(),
                    friendApply.getApplyUser(),
                    null);
            recentChatVOS.add(recentChatVO);
        }
        return recentChatVOS.stream()
                .sorted(Comparator.comparing(RecentChatVO::getLastActiveTime,Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
    }
}
