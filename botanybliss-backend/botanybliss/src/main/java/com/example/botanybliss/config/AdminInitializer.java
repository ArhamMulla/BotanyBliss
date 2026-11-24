package com.example.botanybliss.config;

import com.example.botanybliss.entity.User;
import com.example.botanybliss.entity.Role;
import com.example.botanybliss.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initializeAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${ADMIN_PASSWORD}") String adminPassword) {

        return args -> {
            String adminEmail = "admin@botanybliss.com";

            if (!userRepository.existsByEmail(adminEmail)) {

                User admin = new User();
                admin.setName("Admin");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("Initial admin account created.");
            } else {
                System.out.println("Admin account already exists.");
            }
        };
    }
}