package com.klpdapp.klpd.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.User;

public interface CartRepo extends JpaRepository <Cart, Integer> {

    Optional<Cart> findByProductAndUser(Product product, User user);

}
