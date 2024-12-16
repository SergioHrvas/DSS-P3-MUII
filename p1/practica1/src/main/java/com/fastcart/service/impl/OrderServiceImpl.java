package com.fastcart.service.impl;

import org.springframework.stereotype.Service;
import com.fastcart.model.Order;
import com.fastcart.model.OrderItem;
import com.fastcart.repository.OrderRepo;
import com.fastcart.service.interf.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepository;

    public OrderServiceImpl(OrderRepo orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        Double totalAmount = 0.0;
        List<OrderItem> items = order.getOrderItems();

        for (OrderItem item : items) {
            Integer quantity = item.getQuantity();
            if (quantity == null) {
                quantity = 1; // Valor por defecto si es null
                item.setQuantity(quantity);
            }

            totalAmount += item.getProductPrice() * quantity;
            item.setOrder(order);
        }

        order.setTotalAmount(totalAmount);
        order.setStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + id));
    }

    // Otros métodos si son necesarios
}
