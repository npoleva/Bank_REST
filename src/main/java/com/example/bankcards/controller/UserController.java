package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/users")
public interface UserController {
    @PostMapping
    UserDto createUser(@RequestBody UserDto userDto, @RequestParam String password);

    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable Long id);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    List<UserDto> getAllUsers();

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable Long id);
}
