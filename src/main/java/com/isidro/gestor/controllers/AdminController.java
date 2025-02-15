package com.isidro.gestor.controllers;


import com.isidro.gestor.models.UserModel;
import com.isidro.gestor.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServices userServices;


    @GetMapping("/user")
    public List<UserModel> getAllUsers(){
        return userServices.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public Optional<UserModel> getUserById(@PathVariable Long id){
        return userServices.getUserById(id);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> deleteUSer(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userServices.deleteUser(id, email);

    }
}
