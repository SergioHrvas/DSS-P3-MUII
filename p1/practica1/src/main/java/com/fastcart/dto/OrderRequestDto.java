package com.fastcart.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequestDto {
    private List<ProductDto> products;
    private Double totalAmount;

    public OrderRequestDto() {
    }

    public OrderRequestDto(List<ProductDto> products, Double totalAmount) {
        this.products = products;
        this.totalAmount = totalAmount;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
