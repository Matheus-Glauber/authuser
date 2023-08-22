package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static com.ead.authuser.dtos.response.Messages.IMAGE_UPDATED_SUCCESSFULLY;
import static com.ead.authuser.dtos.response.Messages.PASSWORD_UPDATED_SUCCESSFULLY;
import static com.ead.authuser.enums.UserStatus.ACTIVE;
import static com.ead.authuser.enums.UserType.ADMIN;
import static com.ead.authuser.enums.UserType.STUDENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIntegrationTest extends TestBase {

    private static final String URL_API = "/users";

    @Autowired
    private UserRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Get All Users")
    void get_all_users() throws Exception {
        saveUsers();
        mockMvc.perform(get(URL_API))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.isA(ArrayList.class)))
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.*[0].userId", Matchers.notNullValue()))
                .andExpect(jsonPath("$.*[0].userName", Matchers.notNullValue()))
                .andExpect(jsonPath("$.*[1].userId", Matchers.notNullValue()))
                .andExpect(jsonPath("$.*[1].userName", Matchers.notNullValue()));
    }

    @Test
    @DisplayName("Get User By Id")
    void get_user_by_id() throws Exception {
        saveUsers();
        UserModel userExample = repository.findAll().get(0);
        String url = URL_API + "/" + userExample.getUserId().toString();

        mockMvc.perform(get(url))
                .andExpect(jsonPath("$.*", Matchers.notNullValue()))
                .andExpect(jsonPath("$.*.userId", Matchers.notNullValue()))
                .andExpect(jsonPath("$.*.userName", Matchers.notNullValue()));
    }

    @Test
    @DisplayName("Delete User By Id")
    void delete_by_id() throws Exception {
        saveUsers();
        UserModel userExample = repository.findAll().get(0);
        String url = URL_API + "/" + userExample.getUserId().toString();

        mockMvc.perform(delete(url)
                .param("userId", userExample.getUserId().toString()))
                .andExpect(status().isOk());

        Optional<UserModel> userEmpty = repository.findById(userExample.getUserId());
        Assertions.assertTrue(userEmpty.isEmpty());

    }

    @Test
    @DisplayName("Update User By Id")
    void update_user_by_id() throws Exception {
        UserDto dto = UserDto
                .builder()
                .fullName("Novo Nome")
                .phoneNumber("83988550022")
                .cpf("45698744455")
                .build();

        saveUsers();
        UserModel userExample = repository.findAll().get(0);
        String url = URL_API + "/" + userExample.getUserId().toString();

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value.fullName", Matchers.equalTo("Novo Nome")))
                .andExpect(jsonPath("$.value.phoneNumber", Matchers.equalTo("83988550022")))
                .andExpect(jsonPath("$.value.cpf", Matchers.equalTo("45698744455")));
    }

    @Test
    @DisplayName("Update User Password By Id")
    void update_user_password_by_id() throws Exception {
        saveUsers();

        UserModel userExample = repository.findAll().get(0);
        UserDto dto = UserDto
                .builder()
                .password("asd123")
                .oldPassword(userExample.getPassword())
                .build();
        String password = userExample.getUserId().toString();
        String url = URL_API + "/password/" + password;

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message",
                        Matchers.equalTo(String.format(PASSWORD_UPDATED_SUCCESSFULLY, password))));
    }

    @Test
    @DisplayName("Update User Image By Id")
    void updateImageById() throws Exception {
        saveUsers();

        UserModel userExample = repository.findAll().get(0);
        UserDto dto = UserDto
                .builder()
                .imageUrl("https://img.freepik.com/fotos-gratis/imagem-aproximada-da-cabeca-de-um-lindo-leao_181624-35855.jpg?w=2000")
                .build();
        String userId = userExample.getUserId().toString();
        String url = URL_API + "/image/" + userId;

        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message",
                        Matchers.equalTo(String.format(IMAGE_UPDATED_SUCCESSFULLY, userId))));

    }

    private void saveUsers() {
        UserModel user = UserModel
                .builder()
                .username("Matheus Glauber")
                .email("email@email.com")
                .password("Abc123!")
                .fullName("Matheus Glauber Rodrigues Jordão")
                .userType(ADMIN)
                .userStatus(ACTIVE)
                .phoneNumber("83999999999")
                .cpf("10164585499")
                .creationDate(LocalDateTime.of(2023, 8, 18, 9, 32))
                .lastUpdateDate(LocalDateTime.of(2023, 8, 18, 9, 32))
                .build();
        UserModel user2 = UserModel
                .builder()
                .username("Teste Um")
                .email("teste@email.com")
                .password("Abc222!")
                .fullName("Teste Número Um")
                .userType(STUDENT)
                .userStatus(ACTIVE)
                .phoneNumber("83999999988")
                .cpf("10584785499")
                .creationDate(LocalDateTime.of(2023, 8, 18, 9, 32))
                .lastUpdateDate(LocalDateTime.of(2023, 8, 18, 9, 32))
                .build();
        repository.save(user);
        repository.save(user2);
    }
}