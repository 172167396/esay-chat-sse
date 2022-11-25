package com.easychat.sse.model.vo;

import com.easychat.sse.model.dto.FriendApplyDTO;
import com.easychat.sse.model.entity.FriendApply;
import com.easychat.sse.utils.DateTimeFormatUtil;
import com.easychat.sse.utils.MinioUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class FriendApplyVO {

    private String id;
    private String userName;
    private String avatar;
    private String source;
    private String remark;
    private String createTime;
    /**
     * 0待处理 1已同意 2已拒绝
     */
    private int state;

    public static List<FriendApplyVO> fromDtoList(List<FriendApplyDTO> dtoList) {
        return dtoList.stream().sorted(Comparator.comparing(FriendApply::getCreateTime).reversed())
                .map(dto -> {
                    FriendApplyVO vo = new FriendApplyVO();
                    vo.setId(dto.getId());
                    vo.setRemark(dto.getRemark());
                    vo.setState(dto.getState());
                    vo.setUserName(dto.getUserName());
                    vo.setAvatar(MinioUtil.buildPath(dto.getBucket(), dto.getFileName()));
                    vo.setCreateTime(DateTimeFormatUtil.getDisplayDate(dto.getCreateTime()));
                    vo.setSource("来自QQ查找");
                    return vo;
                }).collect(Collectors.toList());
    }
}
