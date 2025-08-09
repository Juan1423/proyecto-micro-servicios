package com.gestion.publicaciones.auth.service;


import com.gestion.publicaciones.auth.config.JwtUtil;
import com.gestion.publicaciones.auth.dto.LoginResponseDTO; // Import LoginResponseDTO
import com.gestion.publicaciones.auth.dto.UserResponseDTO;
import com.gestion.publicaciones.auth.model.Role;
import com.gestion.publicaciones.auth.model.Usuario;
import com.gestion.publicaciones.auth.repository.RoleRepository;
import com.gestion.publicaciones.auth.repository.UsuarioRepository;
import com.nimbusds.jose.JOSEException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private EventoService eventoService;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, RoleRepository roleRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String registrar(String nombre, String email, String password, Set<String> roleNames) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email ya registrado");
        }

        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

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
            return jwtUtil.generateToken(user.getId(), user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        } catch (JOSEException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Error al generar el token");
        }
        
    }

    public UserResponseDTO getUserById(UUID id) {
        return usuarioRepository.findById(id)
                .map(usuario -> new UserResponseDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail()))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public LoginResponseDTO login(String email, String password) {
        Optional<Usuario> userOpt = usuarioRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        Usuario user = userOpt.get();
        try {
            eventoService.notificarLogin(email);
            String token = jwtUtil.generateToken(user.getId(), user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
            String role = user.getRoles().stream().findFirst().map(Role::getName).orElse("ROLE_LECTOR"); // Get the first role, default to LECTOR
            return new LoginResponseDTO(token, role);
        } catch (JOSEException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar el token");
        }
    }
}
