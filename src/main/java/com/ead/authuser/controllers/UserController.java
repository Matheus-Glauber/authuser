package com.ead.authuser.controllers;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "userId")UUID userId) {
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(!userModelOptional.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");

        return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable(value = "userId")UUID userId) {
        Optional<UserModel> userOptional = userService.findById(userId);

        if(!userOptional.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");

        userService.delete(userOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfuly");
    }
}
