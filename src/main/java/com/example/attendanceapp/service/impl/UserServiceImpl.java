package com.example.attendanceapp.service.impl;

import com.example.attendanceapp.dto.*;
import com.example.attendanceapp.entity.User;
import com.example.attendanceapp.repository.UserRepository;
import com.example.attendanceapp.security.JwtUtil;
import com.example.attendanceapp.service.UserService;
import com.example.attendanceapp.util.EmailSender;
import com.example.attendanceapp.util.OtpGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpGenerator otpGenerator;
    private final EmailSender emailSender;
    private final JwtUtil jwtUtil;
    private final DataSource dataSource;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           OtpGenerator otpGenerator,
                           EmailSender emailSender,
                           JwtUtil jwtUtil,
                           DataSource dataSource) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpGenerator = otpGenerator;
        this.emailSender = emailSender;
        this.jwtUtil = jwtUtil;
        this.dataSource = dataSource;
    }

    @Override
    public UserDTO signup(SignupDTO signupDTO) {
        if (signupDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid signup data");
        if (userRepository.existsByEmail(signupDTO.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        if (userRepository.existsByMobile(signupDTO.getMobile()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mobile already registered");

        User u = new User();
        u.setName(signupDTO.getName());
        u.setEmail(signupDTO.getEmail());
        u.setMobile(signupDTO.getMobile());
        u.setAbout(signupDTO.getAbout());
        u.setPassword(passwordEncoder.encode(signupDTO.getPassword()));

        User saved = userRepository.save(u);
        return new UserDTO(saved.getId(), saved.getName(), saved.getEmail(), saved.getMobile(), saved.getAbout());
    }

    @Override
    public UserDTO login(LoginDTO loginDTO) {
        System.out.println("🔐 Login attempt for: " + loginDTO.getEmailOrMobile());

        User user = userRepository.findByEmail(loginDTO.getEmailOrMobile())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getMobile(), user.getAbout());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getMobile(), u.getAbout()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateProfile(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setMobile(userDTO.getMobile());
        user.setAbout(userDTO.getAbout());

        User updated = userRepository.save(user);
        return new UserDTO(updated.getId(), updated.getName(), updated.getEmail(), updated.getMobile(), updated.getAbout());
    }

    @Override
    public String sendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String otp = otpGenerator.generateOtp();
        user.setOtp(otp);
        userRepository.save(user);

        emailSender.sendOtpEmail(user.getEmail(), otp); // ✅ uses actual user email
        return otp;
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return user.getOtp() != null && user.getOtp().equals(otp);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getMobile(), u.getAbout());
    }

    @Override
    @Transactional
    public String resetPassword(String emailOrMobile, String newPassword) {
        System.out.println("🔧 resetPassword() called for: " + emailOrMobile);

        String encoded = passwordEncoder.encode(newPassword);
        System.out.println("New encoded password: " + encoded);

        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE users SET password = ?, otp = NULL WHERE email = ? OR mobile = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, encoded);
            stmt.setString(2, emailOrMobile);
            stmt.setString(3, emailOrMobile);
            int rows = stmt.executeUpdate();

            System.out.println("✅ JDBC update rows affected: " + rows);
            if (rows == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DB update failed");
        }

        return "Password reset successful";
    }
}
