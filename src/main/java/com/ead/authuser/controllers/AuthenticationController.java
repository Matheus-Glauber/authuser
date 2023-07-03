package com.ead.authuser.controllers;

import com.ead.authuser.controllers.api.AuthenticationApi;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.mapper.UserMapper;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class AuthenticationController implements AuthenticationApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper mapper;

    @Override
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class)
                                                   @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {
        userService.existsByUserName(userDto.username());
        userService.existsByEmail(userDto.email());
        UserModel userModel = mapper.convertToUserModel(userDto);
        userService.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
