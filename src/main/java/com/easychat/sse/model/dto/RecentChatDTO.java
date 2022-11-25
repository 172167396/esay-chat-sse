package com.easychat.sse.model.dto;

import com.easychat.sse.model.entity.RecentChat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecentChatDTO extends RecentChat {
    private String briefMsg;
    private String bucket;
    private String fileName;
}
