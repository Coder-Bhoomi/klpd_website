package com.klpdapp.klpd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.User;

public interface UserRepo extends JpaRepository<User, Integer> {

    User findByEmail(String email);

}
