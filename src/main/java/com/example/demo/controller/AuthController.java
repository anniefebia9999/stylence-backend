package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered");
        }

        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFullName()
        );
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getFullName(), user.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getFullName(), user.getRole()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        // Always return the same message, whether or not the email exists
        // (prevents attackers from discovering which emails are registered)
        if (user == null) {
            return ResponseEntity.ok("If that email exists, a reset link has been sent.");
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(System.currentTimeMillis() + 15 * 60 * 1000); // 15 min expiry
        userRepository.save(user);

        String resetLink = "http://localhost:4200/reset-password?token=" + token;

        try {
            emailService.sendResetEmail(user.getEmail(), resetLink);
        } catch (Exception e) {
            System.out.println("Failed to send reset email: " + e.getMessage());
            // Fallback: still log the link so you can test manually if email sending fails
            System.out.println("PASSWORD RESET LINK for " + user.getEmail() + ": " + resetLink);
        }

        return ResponseEntity.ok("If that email exists, a reset link has been sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        User user = userRepository.findByResetToken(request.getToken()).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid or expired reset link.");
        }

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry() < System.currentTimeMillis()) {
            return ResponseEntity.badRequest().body("Reset link has expired. Please request a new one.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully. You can now log in.");
    }
}