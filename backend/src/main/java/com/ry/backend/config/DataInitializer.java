package com.ry.backend.config;

import com.ry.backend.model.User;
import com.ry.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(DEFAULT_ADMIN_USERNAME).isEmpty()) {
            User admin = new User();
            admin.setUsername(DEFAULT_ADMIN_USERNAME);
            admin.setPassword(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD));
            userRepository.save(admin);
            log.info("Default admin user created: {}/{}", DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD);
        }
    }
}
