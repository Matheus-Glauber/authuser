package com.ead.authuser.controllers.api;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.dtos.response.PayloadResponse;
import com.ead.authuser.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UserApi {

    ResponseEntity<Page<UserModel>> getAll(Pageable pageable);

    ResponseEntity<UserModel> getUserById(UUID userId);

    ResponseEntity deleteById(UUID userId);

    ResponseEntity<PayloadResponse<UserModel>> updateById(UUID userId, UserDto user);
    ResponseEntity<PayloadResponse> updatePasswordById(UUID userId, UserDto user);

    ResponseEntity<PayloadResponse> updateImageById(UUID userId, UserDto user);

}
