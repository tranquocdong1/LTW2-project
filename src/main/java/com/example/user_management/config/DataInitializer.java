package com.example.user_management.config;

import com.example.user_management.entity.Role;
import com.example.user_management.entity.User;
import com.example.user_management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            String adminEmail = "admin@example.com";
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setName("Administrator");
                admin.setEmail(adminEmail);
                admin.setPassword(encoder.encode("admin123")); // mật khẩu mặc định
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Seeded admin user: " + adminEmail + " / admin123");
            } else {
                System.out.println("Admin already exists: " + adminEmail);
            }
        };
    }
}
