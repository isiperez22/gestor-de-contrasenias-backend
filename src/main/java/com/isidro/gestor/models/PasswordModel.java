package com.isidro.gestor.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stored_passwords")
public class PasswordModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String service;
    private String username;
    private String password;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_password", value = ConstraintMode.CONSTRAINT))
    @JsonManagedReference // Controla la serialización de la relación desde el lado de UserModel
    private UserModel user;

    @PrePersist
    protected void onCreate(){
        createAt = LocalDateTime.now();
        updateAt = createAt;
    }

    @PreUpdate
    protected void onUpdate(){
        updateAt = LocalDateTime.now();
    }
}
