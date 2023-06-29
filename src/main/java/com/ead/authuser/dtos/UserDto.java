package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        UUID userId,
        @JsonView(UserView.RegistrationPost.class)
        String username,
        @JsonView(UserView.RegistrationPost.class)
        String email,
        @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
        String password,
        @JsonView(UserView.PasswordPut.class)
        String oldPassword,
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String fullName,
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String phoneNumber,
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String cpf,
        @JsonView({UserView.RegistrationPost.class, UserView.ImagePut.class})
        String imageUrl) {

    public interface UserView {
        interface RegistrationPost{}
        interface UserPut{}
        interface PasswordPut{}
        interface ImagePut{}
    }
}
