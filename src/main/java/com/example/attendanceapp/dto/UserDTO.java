package com.example.attendanceapp.dto;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String about;

    public UserDTO() {}
    public UserDTO(Long id, String name, String email, String mobile, String about) {
        this.id = id; this.name = name; this.email = email; this.mobile = mobile; this.about = about;
    }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getMobile() { return mobile; } public void setMobile(String mobile) { this.mobile = mobile; }
    public String getAbout() { return about; } public void setAbout(String about) { this.about = about; }
}