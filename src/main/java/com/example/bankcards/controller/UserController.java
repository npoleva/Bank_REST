package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "Управление пользователями")
@SecurityRequirement(name = "bearerAuth")
public interface UserController {
    @PostMapping
    @Operation(
            summary = "Создать нового пользователя"
    )
    UserDto createUser(@RequestBody UserDto userDto, @RequestParam String password);

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить пользователя по id"
    )
    UserDto getUserById(@PathVariable Long id);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(
            summary = "Получить всех пользователей",
            description = "Доступно только для администраторов"
    )
    List<UserDto> getAllUsers();

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить пользователя по id",
            description = "Доступно только для администраторов"
    )
    void deleteUser(@PathVariable Long id);
}
