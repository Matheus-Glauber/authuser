package com.ead.authuser.controllers.api;

import com.ead.authuser.dtos.UserDto;
import org.springframework.http.ResponseEntity;

public interface AuthenticationApi {
    ResponseEntity<Object> registerUser(UserDto userDto);
}
