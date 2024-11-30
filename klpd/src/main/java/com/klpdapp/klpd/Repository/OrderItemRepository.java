package com.klpdapp.klpd.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.OrderItem;
import com.klpdapp.klpd.model.User;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrder_User(User user);
    
}
