package com.example.attendanceapp.dto;

public class JwtResponseDTO {
    private String token;
    private UserDTO user;

    public JwtResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public UserDTO getUser() { return user; }
}
