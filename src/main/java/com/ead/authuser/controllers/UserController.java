package com.ead.authuser.controllers;

import com.ead.authuser.controllers.api.UserApi;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.dtos.response.Messages;
import com.ead.authuser.dtos.response.PayloadResponse;
import com.ead.authuser.mapper.UserMapper;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController implements UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper mapper;

    @Override
    @GetMapping
    public ResponseEntity<List<UserModel>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> getUserById(@PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId));
    }

    @Override
    @DeleteMapping("/{userId}")
    public ResponseEntity<PayloadResponse> deleteById(@PathVariable(value = "userId") UUID userId) {
        userService.deleteById(userId);
        return ResponseEntity.ok(PayloadResponse
                .builder()
                .message(String.format(Messages.USER_DELETED_SUCCESSFULLY, userId))
                .httpStatus(HttpStatus.OK)
                .build());
    }

    @Override
    @PutMapping("/{userId}")
    public ResponseEntity<PayloadResponse<UserModel>> updateById(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @JsonView(UserDto.UserView.UserPut.class) UserDto user) {
        UserModel response = mapper.updatedAttributes(userService.findById(userId), user);
        userService.save(response);
        return ResponseEntity.ok(new PayloadResponse<>(
                String.format(Messages.USER_UPDATED_SUCCESSFULLY, userId),
                HttpStatus.OK,
                response));
    }

    @Override
    @PutMapping("/password/{userId}")
    public ResponseEntity<PayloadResponse> updatePasswordById(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @JsonView(UserDto.UserView.PasswordPut.class) UserDto user) {
        UserModel response = userService.findById(userId);
        userService.validPassword(user.password(), response.getPassword());
        userService.save(mapper.updatePassword(response, user));
        return ResponseEntity.ok(PayloadResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format(Messages.PASSWORD_UPDATED_SUCCESSFULLY, userId))
                .build());
    }

    @Override
    @PutMapping("/image/{userId}")
    public ResponseEntity<PayloadResponse> updateImageById(@PathVariable(value = "userId") UUID userId,
                                                     @RequestBody @JsonView(UserDto.UserView.ImagePut.class) UserDto user) {
        UserModel response = userService.findById(userId);
        userService.save(mapper.updateImage(response, user));
        return ResponseEntity.ok(PayloadResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format(Messages.IMAGE_UPDATED_SUCCESSFULLY, userId))
                .build());
    }
}
