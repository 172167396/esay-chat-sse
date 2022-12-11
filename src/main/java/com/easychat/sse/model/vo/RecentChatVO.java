package com.easychat.sse.model.vo;

import com.easychat.sse.enums.RecentMsgType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.easychat.sse.utils.DateTimeFormatUtil.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecentChatVO {
    private String id;
    private String name;
    private String briefMsg;
    private RecentMsgType type;
    private LocalDateTime lastActiveTime;
    private LocalDateTime createTime;
    private String targetId;
    private String avatar;

    public String getChatDate() {
        return getChatLineDisplayDate(lastActiveTime);
    }
}
