package com.example.swproj22.config;

import com.example.swproj22.domain.UserRole;
import com.example.swproj22.domain.entity.User;
import com.example.swproj22.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Adminsave {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByLoginId("admin")) {
                User user = User.builder()
                        .loginId("admin")
                        .password("thdnprhd22")
                        .nickname("a")
                        .role(UserRole.ADMIN)
                        .build();

                userRepository.save(user);
            }
        };
    }
}