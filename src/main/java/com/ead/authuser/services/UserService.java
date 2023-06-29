package com.ead.authuser.services;

import com.ead.authuser.models.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> getAll();

    Optional<UserModel> findById(UUID userId);

    void deleteById(UUID userId);

    void save(UserModel userModel);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);
}
