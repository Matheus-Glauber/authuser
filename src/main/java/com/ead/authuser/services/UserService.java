package com.ead.authuser.services;

import com.ead.authuser.models.UserModel;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserModel> getAll();

    UserModel findById(UUID userId);

    void deleteById(UUID userId);

    void save(UserModel userModel);

    void existsByUserName(String username);

    void existsByEmail(String email);

    void validPassword(String newPassword, String oldPassword);
}
