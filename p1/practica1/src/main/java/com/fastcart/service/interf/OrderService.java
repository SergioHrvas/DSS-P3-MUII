package com.fastcart.service.interf;


import com.fastcart.model.Order;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrderById(Long id);
    // Otros métodos si son necesarios
}