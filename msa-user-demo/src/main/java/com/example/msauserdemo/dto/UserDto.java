package com.example.msauserdemo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDto {
    private String email;
    private String userName;
    private String password;
//    private String roles = "ROLE-STAR, ROLE-FAN";
    private String roles;
}
