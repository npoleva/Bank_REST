package com.example.bankcards.controller;

import com.example.bankcards.controller.UserController;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @Override
    public UserDto createUser(@RequestBody UserDto userDto, @RequestParam String password) {
        return userService.createUser(userDto, password);
    }

    @Override
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
