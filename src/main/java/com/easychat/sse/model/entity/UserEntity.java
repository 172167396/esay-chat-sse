package com.easychat.sse.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.easychat.sse.model.dto.IdTitle;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@TableName("sys_user")
public class UserEntity implements Serializable {
    @TableId
    private String id;

    @TableField("name")
    private String name;

    @TableField("account")
    private String account;

    @TableField("password")
    private String password;

    @TableField("gender")
    private String gender;

    @TableField("address")
    private String address;

    @TableField("avatar_id")
    private String avatarId;

    @TableField("mobile")
    private String mobile;

    @TableField("avatar_path")
    private String avatarPath;

    @TableField("signature")
    private String signature;

    @TableField("birthday")
    private LocalDate birthday;

    @TableField("job")
    private String job;

    @TableField("country")
    private String country;

    @TableField("province")
    private String province;

    @TableField("city")
    private String city;

    @TableField("district")
    private String district;

    @TableField("company")
    private String company;

    @TableField("email")
    private String email;

    @TableField("school")
    private String school;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private List<IdTitle> groups;
}
