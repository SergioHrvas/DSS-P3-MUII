package com.fastcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fastcart.model.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
    // Métodos adicionales si son necesarios
}
