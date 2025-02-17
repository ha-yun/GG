package com.example.msalive.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LiveMessageDto {
    private String username;
    private String role;
    private String message;

    public LiveMessageDto(String username, String role, String message) {
        this.username = username;
        this.role = role;
        this.message = message;
    }

    public String getToken() {
        return username + ":" + role;

    }
}
