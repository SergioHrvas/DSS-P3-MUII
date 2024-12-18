package com.example.myapplication.service

import com.example.myapplication.model.Product

data class OrderRequest(
    val products: List<Product>, val totalAmount: Double
)