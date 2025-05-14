package com.klpdapp.klpd.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Login;
import com.klpdapp.klpd.model.Product;

import jakarta.transaction.Transactional;

public interface CartRepo extends JpaRepository <Cart, Integer> {

    Optional<Cart> findByProductAndUser(Product product, Login user);

    List<Cart> findByUser(Login user);

    @Transactional
    @Modifying
    void deleteByUser(Login user);

}
