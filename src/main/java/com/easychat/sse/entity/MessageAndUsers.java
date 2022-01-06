package com.easychat.sse.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageAndUsers {
    private MessageEntity message;
    private List<UserEntity> onlineUsers;
}
