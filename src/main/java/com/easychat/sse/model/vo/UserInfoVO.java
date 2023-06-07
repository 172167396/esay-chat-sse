package com.easychat.sse.model.vo;

import com.easychat.sse.enums.Gender;
import com.easychat.sse.model.entity.UserEntity;
import com.easychat.sse.utils.MinioUtil;
import com.easychat.sse.utils.NullUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class UserInfoVO {
    private String name;
    private String avatarPath;
    private String signature;
    private String account;
    private String personal;
    /**
     * 所在地
     */
    private String location;
    private String tel;
    private String job;
    private String company;
    private String email;
    private String school;

    public static UserInfoVO from(UserEntity userEntity) {
        UserInfoVO vo = new UserInfoVO();
        vo.setName(userEntity.getName());
        vo.setAvatarPath(MinioUtil.buildPath(userEntity.getAvatarPath()));
        vo.setSignature(userEntity.getSignature());
        vo.setAccount(userEntity.getAccount());
        long age = userEntity.getBirthday() == null ? 0 : ChronoUnit.YEARS.between(userEntity.getBirthday(), LocalDate.now());
        vo.setPersonal(age + "  " + Gender.getNameFromValue(userEntity.getGender()));
        vo.setLocation(NullUtil.nullAsEmpty(userEntity.getCountry())
                + NullUtil.nullAsEmpty(userEntity.getProvince())
                + NullUtil.nullAsEmpty(userEntity.getCity())
                + NullUtil.nullAsEmpty(userEntity.getDistrict()));
        vo.setTel(userEntity.getMobile());
        vo.setJob(userEntity.getJob());
        vo.setCompany(userEntity.getCompany());
        vo.setEmail(userEntity.getEmail());
        vo.setSchool(userEntity.getSchool());
        return vo;
    }

}
