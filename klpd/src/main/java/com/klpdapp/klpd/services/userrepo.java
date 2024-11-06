package com.klpdapp.klpd.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.user;

public interface userrepo extends JpaRepository<user, Integer> {

}
