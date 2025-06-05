package com.example.bankcards.mapper;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        return dto;
    }

    public static User toEntity(UserDto dto, String passwordHash) {
        if (dto == null || passwordHash == null) {
            throw new IllegalArgumentException("DTO and passwordHash must not be null");
        }
        return new User(
                dto.getUsername(),
                passwordHash,
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                Role.valueOf(dto.getRole())
        );
    }
}
