package com.easychat.sse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    FEMALE("0", "女"),
    MALE("1", "男"),
    HIDDEN("2", "隐藏"),
    ;

    private final String value;
    private final String name;

    public static String getNameFromValue(String value) {
        for (Gender gender : values()) {
            if (gender.getValue().equals(value)) {
                return gender.getName();
            }
        }
        return "";
    }
}
