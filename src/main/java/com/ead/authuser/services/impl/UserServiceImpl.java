package com.ead.authuser.services.impl;

import com.ead.authuser.dtos.response.Messages;
import com.ead.authuser.exceptions.InvalidPasswordException;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.exceptions.ParamAlreadyExistsException;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserModel> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserModel findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(Messages.USER_NOT_FOUND, userId)));
    }

    @Override
    public void deleteById(UUID userId) {
        userRepository.delete(findById(userId));
    }

    @Override
    public void save(UserModel userModel) {
        log.info(String.format("User: %s save successfully.", userModel.getUsername()));
        userRepository.save(userModel);
    }

    @Override
    public void existsByUserName(String username) {
        if(userRepository.existsByUsername(username)) {
            throw new ParamAlreadyExistsException(String.format(Messages.USERNAME_ALREADY_EXISTS, username));
        }
    }

    @Override
    public void existsByEmail(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new ParamAlreadyExistsException(String.format(Messages.EMAIL_ALREADY_EXISTS, email));
        }
    }

    @Override
    public void validPassword(String newPassword, String oldPassword) {
        if(!newPassword.equals(oldPassword)) {
            throw new InvalidPasswordException(Messages.INVALID_PASSWORD);
        }
    }
}
