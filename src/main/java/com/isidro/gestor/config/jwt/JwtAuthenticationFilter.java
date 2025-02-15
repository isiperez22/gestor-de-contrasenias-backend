package com.isidro.gestor.config.jwt;

import com.isidro.gestor.models.Role;
import com.isidro.gestor.models.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;


    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        if (requestPath.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            String email = jwtUtil.extractUsername(token);
            System.out.println("Email extraido: " +  email);


            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Verificar el token y configurar el contexto de seguridad
                if (jwtUtil.validateToken(token)) {
                    //Extrear el rol del token
                    Role role = jwtUtil.extractRole(token);

                    //Convertir el rol a una autoridad valida para Spring Security
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));

                    UserModel user = new UserModel();
                    user.setEmail(email);
                    user.setRole(role);

                    // Crear una autenticación basada en el token y agregarla al contexto de seguridad
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            authorities
                    );

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    System.out.println("Usuario autenticado con el rol: " + role);
                } else {
                    System.out.println("Token invalido");
                }
            }
        } else {
            System.out.println("No se encontro el header Authorization");
        }

        filterChain.doFilter(request, response);
    }
}
