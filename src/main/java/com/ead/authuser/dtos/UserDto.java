package com.ead.authuser.dtos;

import com.ead.authuser.validations.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        UUID userId,
        @NotBlank(groups = UserView.RegistrationPost.class,
                message = "The username is required.")
        @Size(groups = UserView.RegistrationPost.class,
                min = 4, max = 500, message = "The username attribute must have a min={min} and max={max} characters.")
        @UsernameConstraint(groups = UserView.RegistrationPost.class,
                message = "The username '${validatedValue}' cannot have spaces.")
        @JsonView(UserView.RegistrationPost.class)
        String username,
        @NotBlank(groups = UserView.RegistrationPost.class,
                message = "The email is required.")
        @Email(groups = UserView.RegistrationPost.class)
        @JsonView(UserView.RegistrationPost.class)
        String email,
        @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class},
                message = "The password is required.")
        @Size(min = 6, max = 20, message = "The '${validatedValue}' value must have a min={min} and max={max} characters.",
                groups = UserView.PasswordPut.class)
        @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
        String password,
        @NotBlank(groups = UserView.PasswordPut.class,
                message = "The oldPassword is required.")
        @Size(min = 6, max = 20, message = "The '${validatedValue}' value must have a min={min} and max={max} characters.",
                groups = UserView.PasswordPut.class)
        @JsonView(UserView.PasswordPut.class)
        String oldPassword,
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String fullName,
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String phoneNumber,
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String cpf,
        @NotBlank(groups = {UserView.RegistrationPost.class, UserView.ImagePut.class},
                message = "The attribute imageUrl is required.")
        @JsonView({UserView.RegistrationPost.class, UserView.ImagePut.class})
        String imageUrl) {

    public interface UserView {
        interface RegistrationPost{}
        interface UserPut{}
        interface PasswordPut{}
        interface ImagePut{}
    }
}
