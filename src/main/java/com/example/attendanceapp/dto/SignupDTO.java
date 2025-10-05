package com.example.attendanceapp.dto;

public class SignupDTO {
    private String name;
    private String email;
    private String mobile;
    private String about;
    private String password;

    public SignupDTO() {}
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getMobile() { return mobile; } public void setMobile(String mobile) { this.mobile = mobile; }
    public String getAbout() { return about; } public void setAbout(String about) { this.about = about; }
    public String getPassword() { return password; } public void setPassword(String password) { this.password = password; }
}
