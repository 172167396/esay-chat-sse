package com.easychat.sse.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ApplyAgreeDTO {
    @NotBlank
    private String id;
    @NotBlank
    private String groupId;
    private String remarkName;
}
