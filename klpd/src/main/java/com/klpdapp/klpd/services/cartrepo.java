package com.klpdapp.klpd.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.cart;

public interface cartrepo extends JpaRepository <cart, Long> {

}
