package com.fastcart.dto;

import com.fastcart.model.Product;

import lombok.Data;

@Data
public class CartItemDto {
	private Long productId;
    private String name;
    private double price;
    private int num;

    public CartItemDto(Product product, int productNum) {
    	this.productId = product.getId();
        this.name = product.getName(); // Ajusta según los campos de tu Product
        this.price = product.getPrice(); // Ajusta según los campos de tu Product
        this.num = productNum;
    }
}