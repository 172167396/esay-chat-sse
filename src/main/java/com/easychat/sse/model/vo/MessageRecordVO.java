package com.easychat.sse.model.vo;

import com.easychat.sse.model.entity.MsgRecordEntity;
import com.easychat.sse.utils.DateTimeFormatUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRecordVO {
    private int type;
    private String content;
    private String fileUrl;
    private String whois;
    private String createTime;

    public static MessageRecordVO from(MsgRecordEntity entity, String searcher) {
        MessageRecordVO vo = new MessageRecordVO();
        vo.setContent(entity.getContent());
        vo.setType(entity.getMessageType());
        vo.setWhois(entity.getSenderId().equals(searcher) ? "me" : "you");
        vo.setFileUrl(entity.getFileId());
        vo.setCreateTime(entity.getCreateTime().format(DateTimeFormatUtil.SLASH_DATE));
        return vo;
    }
}
