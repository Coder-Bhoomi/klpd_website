package com.klpdapp.klpd.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.Login;
import com.klpdapp.klpd.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {


    List<OrderItem> findAllByOrder_User(Login loginuser);

    List<OrderItem> findAllByOrder_UserOrderByOrder_OrderDateDesc(Login loginuser);
    
}
