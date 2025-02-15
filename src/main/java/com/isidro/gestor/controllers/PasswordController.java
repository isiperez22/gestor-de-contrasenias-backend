package com.isidro.gestor.controllers;


import com.isidro.gestor.DTO.PasswordDTO;
import com.isidro.gestor.models.PasswordModel;
import com.isidro.gestor.services.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;


    @PostMapping("/create")
    public PasswordModel createPassword(@RequestBody PasswordDTO passwordModel){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return passwordService.createPassword(passwordModel, email);
    }

    @GetMapping("/my-password")
    public List<PasswordModel> getUserPassword(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return passwordService.getUserPasswords(email);
    }

    @GetMapping("/my-password/{id}")
    public PasswordModel getPasswordById(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return passwordService.getPasswordById(email, id);
    }

    @PutMapping("/my-password/{id}")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody PasswordDTO passwordDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        passwordService.updatePassword(email, id, passwordDTO);

        return ResponseEntity.ok("Se ha actualizado correctamente");
    }

    @DeleteMapping("/my-password/delete/{id}")
    public ResponseEntity<?> deletePassword(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return passwordService.deletePassword(id, email);
    }
}
