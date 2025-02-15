package com.isidro.gestor.DTO;

import com.isidro.gestor.models.Role;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String confirm_password;
    private Role role;
}
