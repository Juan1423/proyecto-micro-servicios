package com.gestion.publicaciones.auth.service;


import com.gestion.publicaciones.auth.config.JwtUtil;
import com.gestion.publicaciones.auth.model.Usuario;
import com.gestion.publicaciones.auth.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String registrar(String nombre, String email, String password, Set<String> roles) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email ya registrado");
        }

        Usuario user = Usuario.builder()
                .id(UUID.randomUUID())
                .nombre(nombre)
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .build();

        usuarioRepository.save(user);
        return jwtUtil.generateToken(user.getId(), user.getRoles());
    }

    public String login(String email, String password) {
        Optional<Usuario> userOpt = usuarioRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        Usuario user = userOpt.get();
        return jwtUtil.generateToken(user.getId(), user.getRoles());
    }
}
