package com.gestion.publicaciones.auth.controller;



import com.gestion.publicaciones.auth.config.JwtUtil;
import com.gestion.publicaciones.auth.service.AuthService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.registrar(request.nombre, request.email, request.password, request.roles);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request.email, request.password);
    }

    @Data
    static class RegisterRequest {
        public String nombre;
        public String email;
        public String password;
        public Set<String> roles;
    }

    @Data
    static class LoginRequest {
        public String email;
        public String password;
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> getJwk() {
        JwtUtil jwtUtil = new JwtUtil();
        return Map.of("keys", List.of(jwtUtil.getJwk().toJSONObject()));
    }

}
