package com.ead.user_service.service;

import com.ead.user_service.util.SupabaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private SupabaseUtil supabaseUtil;

    public ResponseEntity<String> register(String email, String password) {
        return supabaseUtil.signUp(email, password);
    }

    public ResponseEntity<String> login(String email, String password) {
        return supabaseUtil.signIn(email, password);
    }

    public boolean verifyToken(String token) {
        return supabaseUtil.verifyToken(token);
    }

    public ResponseEntity<String> sendPasswordResetEmail(String email) {
        return supabaseUtil.sendPasswordResetEmail(email);
    }

    public ResponseEntity<String> resetPassword(String accessToken, String newPassword) {
        return supabaseUtil.resetPassword(accessToken, newPassword);
    }

    public ResponseEntity<String> verifyOtp(String email, String type, String token) {
        return supabaseUtil.verifyOtp(email, type, token);
    }
}