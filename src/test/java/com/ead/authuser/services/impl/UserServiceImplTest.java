package com.ead.authuser.services.impl;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private EasyRandom easyRandom;

    @BeforeEach
    private void setUp() {
        easyRandom = new EasyRandom();
    }

    @Test
    @DisplayName("Buscar todos os Usuários")
    void findAll() {
        when(userRepository.findAll()).thenReturn(userListMock());

        List<UserModel> response = userService.findAll();
        UserModel userModel = response.get(0);

        assertNotNull(response);
        assertNotNull(userModel);
        assertEquals("Xinxila", userModel.getUserName());
        assertEquals("email@email.com", userModel.getEmail());
        assertEquals("Matheus Glauber Rodrigues Jordão", userModel.getFullname());
        assertEquals(LocalDateTime.of(2022, 8, 5, 21, 10), userModel.getCreationDate());
    }

    @Test
    @DisplayName("Busca um usuário por ID")
    void findById() {
        when(userRepository.findById(any())).thenReturn(Optional.of(userMock()));

        Optional<UserModel> userOptionalResponse = userService.findById(UUID.randomUUID());
        UserModel userModel = userOptionalResponse.get();
        assertTrue(userOptionalResponse.isPresent());
        assertNotNull(userModel);
        assertEquals("Xinxila", userModel.getUserName());
        assertEquals("email@email.com", userModel.getEmail());
        assertEquals("Matheus Glauber Rodrigues Jordão", userModel.getFullname());
        assertEquals(LocalDateTime.of(2022, 8, 5, 21, 10), userModel.getCreationDate());
    }

    @Test
    @DisplayName("Testa a deleção de um usuário")
    void delete() {
        userService.delete(userMock());

        verify(userRepository).delete(any());
    }

    private List<UserModel> userListMock() {
        return Arrays.asList(userMock());
    }

    private static UserModel userMock() {
        return new UserModel(
                UUID.randomUUID(),
                "Xinxila",
                "email@email.com",
                "123456789",
                "Matheus Glauber Rodrigues Jordão",
                UserStatus.ACTIVE,
                UserType.INSTRUCTOR,
                "083988997788",
                "10254898766",
                "imageteste",
                LocalDateTime.of(2022, 8, 5, 21, 10),
                LocalDateTime.of(2022, 8, 5, 21, 10));
    }
}
