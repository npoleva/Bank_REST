package com.example.bankcards.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String password;

    public UserDto toUserDto() {
        UserDto dto = new UserDto();
        dto.setUsername(username);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setEmail(email);
        dto.setRole(role);
        return dto;
    }
}