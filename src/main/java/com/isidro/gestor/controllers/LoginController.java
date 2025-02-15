package com.isidro.gestor.controllers;


import com.isidro.gestor.DTO.LoginRequest;
import com.isidro.gestor.DTO.RegisterUserDTO;
import com.isidro.gestor.models.UserModel;
import com.isidro.gestor.services.AuthService;
import com.isidro.gestor.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserServices userServices;
    private final AuthService authService;

    @PostMapping("/register")
    public UserModel createUser(@RequestBody RegisterUserDTO user) {

        return userServices.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        Map<String , String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
