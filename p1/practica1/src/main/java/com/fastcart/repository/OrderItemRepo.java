package com.fastcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fastcart.model.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
    // Métodos adicionales si son necesarios
}