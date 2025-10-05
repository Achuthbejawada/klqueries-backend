package com.example.attendanceapp.controller;

import com.example.attendanceapp.dto.*;
import com.example.attendanceapp.security.JwtUtil;
import com.example.attendanceapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignupDTO signupDTO) {
        UserDTO user = userService.signup(signupDTO);
        return ResponseEntity.status(201).body(user);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        UserDTO user = userService.login(loginDTO);
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new JwtResponseDTO(token, user));
    }

    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get single user
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        System.out.println("👤 GET /api/users/" + id + " hit");
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Update profile
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateProfile(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateProfile(id, userDTO));
    }

    // Send OTP
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String emailOrMobile) {
        return ResponseEntity.ok(userService.sendOtp(emailOrMobile));
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<Boolean> verifyOtp(@RequestParam String emailOrMobile, @RequestParam String otp) {
        return ResponseEntity.ok(userService.verifyOtp(emailOrMobile, otp));
    }

    // Reset password (supports email or mobile)
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO dto) {
        System.out.println("🚨 RESET endpoint triggered for: " + dto.getEmailOrMobile());
        return ResponseEntity.ok(userService.resetPassword(dto.getEmailOrMobile(), dto.getNewPassword()));
    }
}
