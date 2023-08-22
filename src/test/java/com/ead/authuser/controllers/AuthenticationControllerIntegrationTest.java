package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerIntegrationTest extends TestBase {

    private static final String URL_API = "/users/signup";
    public static final String USERNAME = "fulano23";
    public static final String EMAIL = "fulano_tal@email.com";
    public static final String PASSWORD = "minhasenha123";
    public static final String FULL_NAME = "Fulano de Tal";
    public static final String PHONE_NUMBER = "83988550022";
    public static final String CPF = "45698744455";
    public static final String IMAGE_URL = "https://img.freepik.com/fotos-gratis/imagem-aproximada-da-cabeca-de-um-lindo-leao_181624-35855.jpg?w=2000";

    @Autowired
    private UserRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void registerUser() throws Exception {
        UserDto dto = UserDto
                .builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .fullName(FULL_NAME)
                .phoneNumber(PHONE_NUMBER)
                .cpf(CPF)
                .imageUrl(IMAGE_URL)
                .build();

        mockMvc.perform(post(URL_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", Matchers.equalTo(USERNAME)))
                .andExpect(jsonPath("$.email", Matchers.equalTo(EMAIL)))
                .andExpect(jsonPath("$.fullName", Matchers.equalTo(FULL_NAME)))
                .andExpect(jsonPath("$.phoneNumber", Matchers.equalTo(PHONE_NUMBER)))
                .andExpect(jsonPath("$.cpf", Matchers.equalTo(CPF)))
                .andExpect(jsonPath("$.imageUrl", Matchers.equalTo(IMAGE_URL)));

        UserModel response = repository.findAll().stream().findFirst().orElse(new UserModel());

        assertNotNull(response);
        assertNotNull(response.getUserId());
        assertEquals(USERNAME, response.getUsername());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(FULL_NAME, response.getFullName());
        assertEquals(PHONE_NUMBER, response.getPhoneNumber());
        assertEquals(CPF, response.getCpf());
        assertEquals(IMAGE_URL, response.getImageUrl());
        assertEquals(UserStatus.ACTIVE, response.getUserStatus());
        assertEquals(UserType.STUDENT, response.getUserType());

    }
}