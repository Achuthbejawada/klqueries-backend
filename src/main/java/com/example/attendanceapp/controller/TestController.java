package com.example.attendanceapp.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*")
public class TestController {

    @PostMapping("/ping")
    public String ping(@RequestBody String body) {
        System.out.println("✅ Ping endpoint hit with body: " + body);
        return "pong";
    }
}
