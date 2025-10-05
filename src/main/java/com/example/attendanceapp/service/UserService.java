package com.example.attendanceapp.service;

import com.example.attendanceapp.dto.*;

import java.util.List;

public interface UserService {
    UserDTO signup(SignupDTO signupDTO);
    UserDTO login(LoginDTO loginDTO);
    List<UserDTO> getAllUsers();
    UserDTO updateProfile(Long userId, UserDTO userDTO);
    String sendOtp(String email);
    boolean verifyOtp(String email, String otp);
    String resetPassword(String email, String newPassword);
    UserDTO getUserById(Long id);
}
