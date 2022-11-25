package com.easychat.sse.model.entity;

import com.easychat.sse.model.dto.IdTitle;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserEntity implements Serializable {
    private String id;
    private String name;
    private String account;
    private String password;
    private String gender;
    private String address;
    private String mobile;
    private String avatarId;
    private LocalDateTime createTime;
    private List<IdTitle> groups;
}
