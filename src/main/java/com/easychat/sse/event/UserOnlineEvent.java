package com.easychat.sse.event;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserOnlineEvent{
    private String userId;
}
