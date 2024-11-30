package com.klpdapp.klpd.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.User;

import jakarta.transaction.Transactional;

public interface CartRepo extends JpaRepository <Cart, Integer> {

    Optional<Cart> findByProductAndUser(Product product, User user);

    List<Cart> findByUser(User user);

    @Transactional
    @Modifying
    void deleteByUser(User user);

}
