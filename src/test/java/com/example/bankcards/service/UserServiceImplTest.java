package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createUser_shouldEncodePasswordAndSaveUser() {
        UserDto dto = new UserDto();
        dto.setUsername("Иван username");
        dto.setEmail("email@email.com");
        dto.setFirstName("firstName");
        dto.setLastName("lastName");
        dto.setRole("USER");

        String rawPassword = "password123";

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto created = userService.createUser(dto, rawPassword);

        assertNotNull(created);
        assertEquals("Иван username", created.getUsername());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertNotNull(savedUser.getPasswordHash());
        assertNotEquals(rawPassword, savedUser.getPasswordHash());

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(rawPassword, savedUser.getPasswordHash()));
    }

    @Test
    void getUserById_userExists_shouldReturnDto() {
        User user = new User("Пётр", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto dto = userService.getUserById(1L);

        assertNotNull(dto);
        assertEquals("Пётр", dto.getUsername());
    }

    @Test
    void getUserById_userNotFound_shouldReturnUserNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(99L));

        assertEquals("Пользователь с id 99 не найден", ex.getMessage());
    }

    @Test
    void getAllUsers_shouldReturnListOfDtos() {
        User user1 = new User("Юзер1", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        user1.setId(1L);

        User user2 = new User("Юзер2", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        user2.setId(2L);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDto> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("Юзер1", users.get(0).getUsername());
        assertEquals("Юзер2", users.get(1).getUsername());
    }

    @Test
    void deleteUser_shouldCallRepositoryDelete() {
        when(userRepository.existsById(5L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(5L);

        userService.deleteUser(5L);

        verify(userRepository).deleteById(5L);
    }
}
