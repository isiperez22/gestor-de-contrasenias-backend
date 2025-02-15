package com.isidro.gestor.services;


import com.isidro.gestor.config.jwt.JwtUtil;
import com.isidro.gestor.models.UserModel;
import com.isidro.gestor.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String email, String password) {
        UserModel user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Contraseña no valida");
        }

        return JwtUtil.generateToken(user.getEmail(), user.getRole());
    }
}
