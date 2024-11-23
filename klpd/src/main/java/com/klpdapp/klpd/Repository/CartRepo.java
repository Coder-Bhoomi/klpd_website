package com.klpdapp.klpd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Product;

public interface CartRepo extends JpaRepository <Cart, Integer> {

    Cart findByProduct(Product product);

}
