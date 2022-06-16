package com.easychat.sse.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginInfo {

    private String account;

    private int userCount;

}
