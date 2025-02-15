package com.isidro.gestor.controllers;


import com.isidro.gestor.DTO.UserPasswordDTO;
import com.isidro.gestor.models.UserModel;
import com.isidro.gestor.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServices userServices;

    @PutMapping("/")
    public UserModel updateUserPassword(@RequestBody UserPasswordDTO userPasswordDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")) {
            throw new  ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no autenticado");
        }

        String email = authentication.getName();

        return userServices.updateUserPassword(userPasswordDTO, email);
    }

    @GetMapping("/me")
    public UserModel getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")) {
            throw new  ResponseStatusException(HttpStatus.UNAUTHORIZED, "No estas autorizado");
        }
        String email = authentication.getName();
        UserModel user = userServices.getUserByEmail(email);

        return user;
    }

}
