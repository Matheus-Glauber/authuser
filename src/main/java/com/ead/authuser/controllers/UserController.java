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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.ead.authuser.dtos.response.Messages.USER_DELETED_SUCCESSFULLY;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<Page<UserModel>> getAll(@PageableDefault(sort = "userId", direction = Sort.Direction.ASC)
                                                  Pageable pageable) {
        Page<UserModel> userModelPage = userService.getAll(pageable);
        if (!userModelPage.isEmpty()) {
            for (UserModel user : userModelPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getUserById(user.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.ok(userModelPage);
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
                .message(String.format(USER_DELETED_SUCCESSFULLY, userId))
                .httpStatus(HttpStatus.OK)
                .build());
    }

    @Override
    @PutMapping("/{userId}")
    public ResponseEntity<PayloadResponse<UserModel>> updateById(@PathVariable(value = "userId") UUID userId,
                                             @Validated(UserDto.UserView.UserPut.class)
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
                                            @Validated(UserDto.UserView.PasswordPut.class)
                                            @RequestBody @JsonView(UserDto.UserView.PasswordPut.class) UserDto user) {
        UserModel response = userService.findById(userId);
        userService.validPassword(user.oldPassword(), response.getPassword());
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
                                                    @Validated(UserDto.UserView.ImagePut.class)
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
