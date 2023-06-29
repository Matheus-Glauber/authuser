package com.ead.authuser.controllers.api;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserApi {

    ResponseEntity<List<UserModel>> getAll();

    ResponseEntity<Object> getUserById(UUID userId);

    ResponseEntity deleteById(UUID userId);

    ResponseEntity<Object> updateById(UUID userId, UserDto user);
    ResponseEntity<Object> updatePasswordById(UUID userId, UserDto user);

    ResponseEntity<Object> updateImageById(UUID userId, UserDto user);

}
