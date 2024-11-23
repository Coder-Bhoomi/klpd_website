package com.klpdapp.klpd.Repository;

import com.klpdapp.klpd.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}

