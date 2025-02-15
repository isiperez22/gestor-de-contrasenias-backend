package com.isidro.gestor.repositories;

import com.isidro.gestor.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    UserModel findByFirstName(String firstName);

    UserModel findByEmail(String email);
}
