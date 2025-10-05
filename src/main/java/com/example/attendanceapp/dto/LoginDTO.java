package com.example.attendanceapp.dto;

public class LoginDTO {
    private String emailOrMobile;
    private String password;

    public LoginDTO() {}

    public String getEmailOrMobile() {
        return emailOrMobile;
    }

    public void setEmailOrMobile(String emailOrMobile) {
        this.emailOrMobile = emailOrMobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
