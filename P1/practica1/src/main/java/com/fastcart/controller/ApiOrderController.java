package com.fastcart.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors; // Importar Collectors

import com.fastcart.dto.OrderRequestDto;
import com.fastcart.model.Order;
import com.fastcart.model.OrderItem;
import com.fastcart.model.OrderResponse;
import com.fastcart.service.interf.OrderService;
@RestController
@RequestMapping("/api/orders")
public class ApiOrderController {

    private final OrderService orderService;

    public ApiOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Endpoint para crear una orden con respuesta en formato OrderResponse
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto orderRequest) {
        try {
            // Convertir OrderRequestDto a Order
            Order orderFromRequest = new Order();
            orderFromRequest.setTotalAmount(orderRequest.getTotalAmount());
            orderFromRequest.setStatus("PENDING");

            // Crear lista de OrderItems a partir de los productos del dto
            List<OrderItem> orderItems = orderRequest.getProducts().stream().map(prodDto -> {
                OrderItem item = new OrderItem();
                item.setProductName(prodDto.getName());
                item.setProductPrice(prodDto.getPrice());
                item.setQuantity(1); // Asignar cantidad por defecto o según lógica
                item.setOrder(orderFromRequest);
                return item;
            }).collect(Collectors.toList());

            orderFromRequest.setOrderItems(orderItems);

            // Crear la orden con el servicio
            Order createdOrder = orderService.createOrder(orderFromRequest);

            // Construir la respuesta con el formato esperado por la app
            OrderResponse response = new OrderResponse("true", createdOrder.getId(), "Compra realizada con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // En caso de error, devolver una respuesta con status="false"
            OrderResponse response = new OrderResponse("false", null, "Error al procesar la compra: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Otros endpoints si son necesarios...
}
