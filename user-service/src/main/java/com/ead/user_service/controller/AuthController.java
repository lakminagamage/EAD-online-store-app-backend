package com.ead.user_service.controller;

import com.ead.user_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        return authService.register(email, password);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        return authService.login(email, password);
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
        // Extract access token from the response
        // String accessToken = extractAccessToken(response.getBody());
        // return ResponseEntity.ok(accessToken);
        return response;
    }

    // private String extractAccessToken(String responseBody) {
    //     // Parse the response body to extract the access token
    //     // This is a placeholder implementation. You need to parse the actual response.
    //     // Assuming the response body is a JSON object containing the access token
    //     // Example: {"access_token": "YOUR_ACCESS_TOKEN"}
    //     // You can use a JSON library like Jackson or Gson to parse the response
    //     return ""; // Add appropriate logic here
    // }
}