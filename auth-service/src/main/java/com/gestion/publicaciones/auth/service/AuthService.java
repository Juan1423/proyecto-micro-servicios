package com.gestion.publicaciones.auth.service;


import com.gestion.publicaciones.auth.config.JwtUtil;
import com.gestion.publicaciones.auth.model.Usuario;
import com.gestion.publicaciones.auth.repository.UsuarioRepository;
import com.nimbusds.jose.JOSEException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private EventoService eventoService;
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
        try {
             eventoService.notificarRegistro(user.getEmail());
            return jwtUtil.generateToken(user.getId(), user.getRoles());
        } catch (JOSEException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Error al generar el token");
        }
        
    }

    public String login(String email, String password) {
        Optional<Usuario> userOpt = usuarioRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        Usuario user = userOpt.get();
        try {
            eventoService.notificarLogin(email);
            return jwtUtil.generateToken(user.getId(), user.getRoles());
        } catch (JOSEException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Error al generar el token");
        }
    }
}
