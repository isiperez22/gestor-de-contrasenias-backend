package com.isidro.gestor.services;


import com.isidro.gestor.DTO.PasswordDTO;
import com.isidro.gestor.models.PasswordModel;
import com.isidro.gestor.models.UserModel;
import com.isidro.gestor.repositories.PasswordRepository;
import com.isidro.gestor.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordRepository passwordRepository;
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;



    //Metodo para crear contraseñas
    public PasswordModel createPassword(PasswordDTO passwordDTO, String email){

        Optional<UserModel> userModel = Optional.ofNullable(userRepository.findByEmail(email));

        PasswordModel passwordModel = new PasswordModel();

        if (userModel.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tienes permisos para crear la contraseña");
        }

        String encryptedPassword  = encryptionService.encrypt(passwordDTO.getPassword());

        passwordModel.setUser(userModel.get());
        passwordModel.setService(passwordDTO.getService());
        passwordModel.setUsername(passwordDTO.getUsername());
        passwordModel.setPassword(encryptedPassword);

        return passwordRepository.save(passwordModel);
    }

    //Ver contraseñas de usuarios loggeados
    public List<PasswordModel> getUserPasswords(String email) {
        Optional<UserModel> user = Optional.ofNullable(userRepository.findByEmail(email));

        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        List<PasswordModel> passwords = passwordRepository.findByUser(user.get());

        passwords.forEach(password -> password.setPassword(encryptionService.decrypt(password.getPassword())));

        return passwords;
    }

    public PasswordModel getPasswordById(String email, Long id) {
        Optional<UserModel> user = Optional.ofNullable(userRepository.findByEmail(email));

        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        Optional<PasswordModel> passwordOpt = passwordRepository.findById(id);

        if (passwordOpt.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contraseña no encontrada");
        }

        PasswordModel password = passwordOpt.get();

        password.setPassword(encryptionService.decrypt(password.getPassword()));

        return password;
    }


    //Actualizar password almacenada
    public PasswordModel updatePassword(String email, Long id, PasswordDTO passwordDTO) {
        Optional<UserModel> user = Optional.ofNullable(userRepository.findByEmail(email));

        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        // Buscar la contraseña por ID
        PasswordModel password = passwordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contraseña no encontrada"));

        // Verificar que la contraseña pertenece al usuario autenticado
        if (password.getUser().getId() != user.get().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para actualizar esta contraseña");
        }

        String encryptedPassword = encryptionService.encrypt(passwordDTO.getPassword());

        password.setService(passwordDTO.getService());
        password.setUsername(passwordDTO.getUsername());
        password.setPassword(encryptedPassword);
        password.setUpdateAt(LocalDateTime.now());

        return passwordRepository.save(password);
    }

    public ResponseEntity<?> deletePassword(Long id, String email){
        Optional<UserModel> user = Optional.ofNullable(userRepository.findByEmail(email));

        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        // Buscar la contraseña por ID
        PasswordModel password = passwordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contraseña no encontrada"));

        // Verificar que la contraseña pertenece al usuario autenticado
        if (password.getUser().getId() != user.get().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para actualizar esta contraseña");
        }

        passwordRepository.deleteById(password.getId());

        return ResponseEntity.ok(Collections.singletonMap("message", "Servicio eliminado correctamente"));
    }
}
