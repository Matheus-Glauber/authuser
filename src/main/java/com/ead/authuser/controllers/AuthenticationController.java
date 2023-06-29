package com.ead.authuser.controllers;

import com.ead.authuser.controllers.api.AuthenticationApi;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class AuthenticationController implements AuthenticationApi {

    @Autowired
    UserService userService;

    @Override
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody
                                                   @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {
        if(userService.existsByUserName(userDto.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Param Username already exist in database");
        }
        if(userService.existsByEmail(userDto.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Param email already exist in database");
        }
        UserModel userModel = convertToUserModel(userDto);
        userService.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

    private static UserModel convertToUserModel(UserDto userDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userModel;
    }
}