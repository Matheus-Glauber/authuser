package com.ead.authuser.controllers;

import com.ead.authuser.controllers.api.UserApi;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController implements UserApi {

    @Autowired
    private UserService userService;

    @Override
    @GetMapping
    public ResponseEntity<List<UserModel>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
    }

    @Override
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
        userService.deleteById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Override
    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateById(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @JsonView(UserDto.UserView.UserPut.class) UserDto user) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
        var userModel = userModelOptional.get();
        userModel.setFullName(user.fullName());
        userModel.setPhoneNumber(user.phoneNumber());
        userModel.setCpf(user.cpf());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        return ResponseEntity.ok(userModel);
    }

    @Override
    @PutMapping("/password/{userId}")
    public ResponseEntity<Object> updatePasswordById(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @JsonView(UserDto.UserView.PasswordPut.class) UserDto user) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
        if(!userModelOptional.get().getPassword().equals(user.oldPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mismatched old password!");
        }
        var userModel = userModelOptional.get();
        userModel.setPassword(user.password());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        return ResponseEntity.ok("Password updated successfully.");
    }

    @Override
    @PutMapping("/image/{userId}")
    public ResponseEntity<Object> updateImageById(@PathVariable(value = "userId") UUID userId,
                                                     @RequestBody @JsonView(UserDto.UserView.ImagePut.class) UserDto user) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
        var userModel = userModelOptional.get();
        userModel.setImageUrl(user.imageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        return ResponseEntity.ok("Image updated successfully.");
    }
}
