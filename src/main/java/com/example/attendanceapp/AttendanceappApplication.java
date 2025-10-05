package com.example.attendanceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.attendanceapp.repository")
public class AttendanceappApplication {
    public static void main(String[] args) {
        SpringApplication.run(AttendanceappApplication.class, args);
    }
}
