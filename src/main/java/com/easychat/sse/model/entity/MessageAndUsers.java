package com.easychat.sse.model.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageAndUsers {
    private MessageEntity message;
    private List<OldUserEntity> onlineUsers;
}
