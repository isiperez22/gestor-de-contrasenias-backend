package com.isidro.gestor.repositories;

import com.isidro.gestor.models.PasswordModel;
import com.isidro.gestor.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<PasswordModel, Long> {

    List<PasswordModel> findByUser(UserModel user);
}
