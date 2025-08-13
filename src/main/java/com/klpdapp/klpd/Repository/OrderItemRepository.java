package com.klpdapp.klpd.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.Login;
import com.klpdapp.klpd.model.OrderItem;
import com.klpdapp.klpd.model.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findAllByOrder_UserOrderByOrder_OrderDateDesc(Login loginuser);

    List<OrderItem> findByOrderByOrder_OrderDateDesc();

    boolean existsByOrder_UserAndProduct(Login user, Product product);
    
}
