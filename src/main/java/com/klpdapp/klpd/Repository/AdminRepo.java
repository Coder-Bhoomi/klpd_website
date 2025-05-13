package com.klpdapp.klpd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.Admin;

public interface AdminRepo extends JpaRepository<Admin, Integer>{

    Admin findByEmail(String email);
    
}
