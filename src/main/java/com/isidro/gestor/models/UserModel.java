package com.isidro.gestor.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // Evita la serialización recursiva desde el lado de PasswordModel
    private List<PasswordModel> passwords = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role.name()); // Retorna el rol con prefijo "ROLE_"
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Puedes modificarlo según tu lógica de expiración
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Puedes modificarlo según tu lógica de bloqueo
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Puedes modificarlo según tu lógica de credenciales
    }

    @Override
    public boolean isEnabled() {
        return true; // Puedes modificarlo según si el usuario está habilitado o no
    }

}
