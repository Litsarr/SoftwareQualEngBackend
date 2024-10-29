package com.sqe.finals.service;

import com.sqe.finals.entity.Admin;
import com.sqe.finals.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }
}
