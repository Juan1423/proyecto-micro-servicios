package com.gestion.publicaciones.auth.config;

import com.gestion.publicaciones.auth.model.Role;
import com.gestion.publicaciones.auth.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Ensure predefined roles exist
        Arrays.asList(
                Role.ROLE_AUTOR,
                Role.ROLE_REVISOR,
                Role.ROLE_EDITOR,
                Role.ROLE_ADMIN,
                Role.ROLE_LECTOR
        ).forEach(roleName -> {
            if (roleRepository.findByName(roleName).isEmpty()) {
                roleRepository.save(new Role(roleName));
                System.out.println("Created role: " + roleName);
            }
        });
    }
}