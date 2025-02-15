package com.isidro.gestor.services;


import com.isidro.gestor.DTO.RegisterUserDTO;
import com.isidro.gestor.DTO.UserPasswordDTO;
import com.isidro.gestor.models.UserModel;
import com.isidro.gestor.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServices {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    //Metodo para crear un usuario
    public UserModel createUser(RegisterUserDTO user) {

        if (user.getEmail() == null) {
            return null;
        }

        if (!user.getPassword().equals(user.getConfirm_password())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseñas son distintas");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        UserModel newUser = new UserModel();

        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(encodedPassword);
        newUser.setRole(user.getRole());

        return userRepository.save(newUser);
    }

    //Ver todos los usuarios
    public List<UserModel> getAllUsers(){
        return userRepository.findAll();
    }

    //Ver usuario por id
    public Optional<UserModel> getUserById(Long id){

        Optional<UserModel> userModel = userRepository.findById(id);

        if (userModel.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        return userModel;
    }

    //Ver usuario por email
    public UserModel getUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email);

        return user;
    }

    //Eliminar usuario
    public ResponseEntity<?> deleteUser(Long id, String email){
        Optional<UserModel> userModel = userRepository.findById(id);
        UserModel isAdmin = userRepository.findByEmail(email);

        if (userModel.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        if (isAdmin.getRole().name() != "ADMIN"){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes perimisos para realizar esta accion");
        }

        userRepository.deleteById(id);

        return ResponseEntity.ok(Collections.singletonMap("message", "Servicio eliminado correctamente"));
    }


    //Actualizar contraseña de usuario
    public UserModel updateUserPassword(UserPasswordDTO userPasswordDTO, String email){

        UserModel userModel = userRepository.findByEmail(email);

        if (userModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        if (!userPasswordDTO.getPassword().equals(userPasswordDTO.getConfirm_password())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        String encodedPassword = passwordEncoder.encode(userPasswordDTO.getPassword());

        userModel.setPassword(encodedPassword);

        return userRepository.save(userModel);
    }

}
