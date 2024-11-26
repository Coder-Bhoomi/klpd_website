package com.klpdapp.klpd.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.User;
import com.klpdapp.klpd.model.Wishlist;

public interface WishlistRepo extends JpaRepository<Wishlist, Integer> {

    Optional<Wishlist> findByProductAndUser(Product product, User user);

    List<Wishlist> findAllByUser(User user);
}
