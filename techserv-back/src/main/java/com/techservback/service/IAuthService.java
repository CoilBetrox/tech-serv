package com.techservback.service;

import com.techservback.repository.model.User;

public interface IAuthService {
    String login(String email, String password);
    User registerUser(String email, String password, String role);
    void createPasswordResetToken(String email);
    void resetPassword(String token, String newPassword);
}
