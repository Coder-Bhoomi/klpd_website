package com.klpdapp.klpd.Repository;

import com.klpdapp.klpd.model.Login;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepo extends JpaRepository<Login, Integer> {

    Optional<Login> findByEmail(String email);

    Optional<Login> findByUserId(Integer userId);

}
