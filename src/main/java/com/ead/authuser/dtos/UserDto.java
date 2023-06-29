package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        UUID userId,
        String username,
        String email,
        String password,
        String oldPassword,
        String fullName,
        String phoneNumber,
        String cpf,
        String imageUrl) {}
