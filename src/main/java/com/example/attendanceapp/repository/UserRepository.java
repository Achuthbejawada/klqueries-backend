package com.example.attendanceapp.repository;

import com.example.attendanceapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile); // ✅ Added

    @Query("SELECT u FROM User u WHERE u.email = :input OR u.mobile = :input")
    Optional<User> findByEmailOrMobile(@Param("input") String input);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u SET u.password = :password, u.otp = null WHERE u.email = :email")
    void updatePasswordByEmail(@Param("email") String email, @Param("password") String password); // ✅ Bug 2 fix
}
