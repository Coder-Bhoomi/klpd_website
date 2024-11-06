package com.klpdapp.klpd.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.product;

public interface productrepo extends JpaRepository<product , String> {

}
