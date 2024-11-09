package com.klpdapp.klpd.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.cart;
import com.klpdapp.klpd.model.product;

public interface cartrepo extends JpaRepository <cart, Long> {

    cart findByProduct(product product);

}
