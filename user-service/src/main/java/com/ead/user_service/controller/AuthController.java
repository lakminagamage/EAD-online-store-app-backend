package com.ead.user_service.controller;

import com.ead.user_service.dto.UserCreateDTO;
import com.ead.user_service.dto.UserDTO;
import com.ead.user_service.dto.UserSigninDTO;
import com.ead.user_service.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        UserDTO newUser = authService.register(userCreateDTO);
        if (newUser == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(authService.register(userCreateDTO));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserSigninDTO> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        UserSigninDTO userSigninDTO = authService.login(email, password);
        if (userSigninDTO == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(userSigninDTO);
        }
    }

    @PostMapping("/send-password-reset-email")
    public ResponseEntity<String> sendPasswordResetEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        return authService.sendPasswordResetEmail(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> payload) {
        String accessToken = payload.get("accessToken");
        String newPassword = payload.get("newPassword");
        return authService.resetPassword(accessToken, newPassword);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String type = payload.get("type");
        String token = payload.get("token");
        ResponseEntity<String> response = authService.verifyOtp(email, type, token);

        String accessToken = extractAccessToken(response.getBody());
        return ResponseEntity.ok(accessToken);
    }

    private String extractAccessToken(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.path("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle this appropriately in your application
        }
    }
}