package com.example.msauserdemo.dto;

import lombok.Data;

@Data
public class LoginReqDto {
    private String email;
    private String password;
    private String role;
}
