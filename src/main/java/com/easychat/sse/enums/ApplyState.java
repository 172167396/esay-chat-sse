package com.easychat.sse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplyState {

    NOT_HANDLE(0,"待处理"),
    AGREED(1,"已同意"),
    REFUSED(2,"已拒绝"),
    IGNORED(3,"已忽略");

    final int state;
    final String name;
}
