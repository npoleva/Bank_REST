package com.example.bankcards.service;


import com.example.bankcards.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto dto, String password);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    void deleteUser(Long id);
}
