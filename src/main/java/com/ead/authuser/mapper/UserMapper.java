package com.ead.authuser.mapper;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class UserMapper {

    private static final String ZONE_ID = "UTC";

    public UserModel updatedAttributes(UserModel old, UserDto dto) {
        old.setFullName(dto.fullName());
        old.setPhoneNumber(dto.phoneNumber());
        old.setCpf(dto.cpf());
        old.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        return old;
    }

    public UserModel updatePassword(UserModel old, UserDto dto) {
        old.setPassword(dto.password());
        old.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        return old;
    }

    public UserModel updateImage(UserModel old, UserDto dto) {
        old.setImageUrl(dto.imageUrl());
        old.setLastUpdateDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        return old;
    }

    public UserModel convertToUserModel(UserDto userDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userModel;
    }
}
