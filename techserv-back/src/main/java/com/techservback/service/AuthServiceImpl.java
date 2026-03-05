package com.techservback.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techservback.repository.IUserRepository;
import com.techservback.repository.model.User;
import com.techservback.security.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements IAuthService {

    final private IUserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final private JwtTokenProvider jwtTokenProvider;
    final private JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    public AuthServiceImpl(
        IUserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtTokenProvider jwtTokenProvider,
        JavaMailSender mailSender
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mailSender = mailSender;
    }

    @Override
    @Transactional(readOnly = true)
    public String login(String email, String password) {
        log.info("Attempting login for email: {}", email);
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.error("User not found with email: {}", email);
                return new RuntimeException("User not found");
            });
        
        log.info("User found: ID={}, Email={}", user.getId(), user.getEmail());
        log.info("Stored password hash: {}", user.getPassword());
        log.info("Input password: {}", password);
        
        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        log.info("Password matches: {}", passwordMatches);
        
        if (!passwordMatches) {
            log.error("Invalid password for user: {}", email);
            throw new RuntimeException("Invalid password");
        }

        log.info("Login successful, generating token for user: {}", email);
        return jwtTokenProvider.generateToken(user);
    }

    @Override
    @Transactional
    public User registerUser(String email, String password, String role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        // Respuesta neutra para no filtrar existencia de usuarios.
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole()) || Boolean.FALSE.equals(user.getIsActive())) {
            return;
        }

        String rawToken = UUID.randomUUID().toString();
        user.setResetToken(rawToken);
        user.setTokenExpiration(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        String frontendBaseUrl = resolveFrontendBaseUrl();

        String resetUrl = frontendBaseUrl + "/admin/reset-password?token="
            + URLEncoder.encode(rawToken, StandardCharsets.UTF_8);

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("Recuperación de contraseña - TechService");
        mail.setText(
            "Hola,\n\n"
                + "Recibimos una solicitud para restablecer tu contraseña.\n"
                + "Usa el siguiente enlace:\n"
                + resetUrl + "\n\n"
                + "Este enlace expira en 30 minutos.\n"
                + "Si no solicitaste este cambio, ignora este correo."
        );

        mailSender.send(mail);
    }

    private String resolveFrontendBaseUrl() {
        if (frontendUrl == null || frontendUrl.isBlank()) {
            return "http://localhost:5173";
        }

        String[] candidates = frontendUrl.split(",");
        for (String candidate : candidates) {
            String url = candidate.trim();
            if (!url.isEmpty()) {
                return url;
            }
        }

        return "http://localhost:5173";
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (user.getTokenExpiration() == null || user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token ha expirado");
        }

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new RuntimeException("Usuario inactivo");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiration(null);
        userRepository.save(user);
    }

}
