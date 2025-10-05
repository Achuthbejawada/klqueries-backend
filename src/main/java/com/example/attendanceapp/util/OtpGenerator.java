package com.example.attendanceapp.util;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class OtpGenerator {
    public String generateOtp() {
        Random rnd = new Random();
        int n = 1000 + rnd.nextInt(9000);
        return String.valueOf(n);
    }
}
