package com.example.msauserdemo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDto {
    private String email;
    private String userName;
    private String password;
    private String roles;

    public UserDto(String email, String roles, String username) {
        this.email = email;
        this.roles = roles;
        this.userName = username;

    }
}
