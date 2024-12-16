package com.fastcart.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {
    private Long id;
    private String name;
    private Double price;
    private String imagePath; // opcional si el JSON lo trae, si no, puedes omitirlo.

    public ProductDto() {
    }

    public ProductDto(Long id, String name, Double price, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) { 
        this.price = price; 
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) { 
        this.imagePath = imagePath; 
    }
}