package com.fastcart.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private Double productPrice;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    public OrderItem() {
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
         this.id = id;
    }

    public String getProductName() {
         return productName;
    }

    public void setProductName(String productName) {
         this.productName = productName;
    }

    public Double getProductPrice() {
         return productPrice;
    }

    public void setProductPrice(Double productPrice) {
         this.productPrice = productPrice;
    }

    public Integer getQuantity() {
         return quantity;
    }

    public void setQuantity(Integer quantity) {
         this.quantity = quantity;
    }

    public Order getOrder() {
         return order;
    }

    public void setOrder(Order order) {
         this.order = order;
    }
}
