package com.easychat.sse.event;

import com.easychat.sse.model.domain.UserDomain;
import lombok.*;

@Value
public class UserUpdateEvent {
    UserDomain user;
}
