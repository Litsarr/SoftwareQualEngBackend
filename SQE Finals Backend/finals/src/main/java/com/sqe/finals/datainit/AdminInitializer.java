package com.sqe.finals.datainit;

import com.sqe.finals.entity.Admin;
import com.sqe.finals.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer {

    @Autowired
    private AdminRepository adminRepository;

    @Bean
    public CommandLineRunner initAdmin() {
        return args -> {
            if (adminRepository.count() == 0) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                Admin admin = new Admin("adminUsername", encoder.encode("adminPassword"), "ADMIN");
                adminRepository.save(admin);
            }
        };
    }
}

