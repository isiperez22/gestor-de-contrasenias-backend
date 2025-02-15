package com.isidro.gestor.config.jwt;

import com.isidro.gestor.models.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "7SqJjvvIdy49gWiLJ93vGnomQ37lVd90dV+8JQTdEdc="; // Clave de segrida


    public static String generateToken(String email, Role role) {
        return Jwts.builder()
                .setSubject(email) // Añadimos el email como subject'
                .claim("role", role.name()) //Añadimos el Role
                .setIssuedAt(new Date())  // Fecha de emisión del token
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))  // Tiempo de expiracion del token
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // Usamos HS256 para la firma
                .compact();  // Devuelve el token JWT en formato compactado
    }



    //Extraer el rol del usuario
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Extraer el rol del token
    public Role extractRole(String token) {
        Claims claims = extractAllClaims(token);
        String roleString = claims.get("role", String.class);

        return Role.valueOf(roleString);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    // Extraer todos los claims del token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validar si el token es válido
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
