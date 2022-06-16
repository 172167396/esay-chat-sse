package com.easychat.sse.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FriendGroups extends IdName{
    private List<FriendGroups> friends;
}
