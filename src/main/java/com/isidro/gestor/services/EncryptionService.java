package com.isidro.gestor.services;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12; // Longitud recomendada del IV

    // Clave secreta para el cifrado (DEBE guardarse en una variable de entorno en producción)
    private static final String SECRET_KEY = "1234567890123456"; // 16 bytes = 128 bits

    private SecretKey getKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
    }

    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[IV_LENGTH_BYTE];
            secureRandom.nextBytes(iv);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

            cipher.init(Cipher.ENCRYPT_MODE, getKey(), parameterSpec);
            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Combina IV + datos cifrados en Base64
            byte[] combined = new byte[IV_LENGTH_BYTE + encryptedData.length];
            System.arraycopy(iv, 0, combined, 0, IV_LENGTH_BYTE);
            System.arraycopy(encryptedData, 0, combined, IV_LENGTH_BYTE, encryptedData.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar la contraseña", e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            byte[] iv = new byte[IV_LENGTH_BYTE];
            byte[] encryptedBytes = new byte[decoded.length - IV_LENGTH_BYTE];

            System.arraycopy(decoded, 0, iv, 0, IV_LENGTH_BYTE);
            System.arraycopy(decoded, IV_LENGTH_BYTE, encryptedBytes, 0, encryptedBytes.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.DECRYPT_MODE, getKey(), parameterSpec);

            return new String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al descifrar la contraseña", e);
        }
    }
}
