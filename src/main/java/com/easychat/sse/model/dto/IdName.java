package com.easychat.sse.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class IdName implements Serializable {
    private String id;
    private String name;
}
