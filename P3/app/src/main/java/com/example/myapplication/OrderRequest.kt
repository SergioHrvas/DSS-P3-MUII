package com.example.myapplication

data class OrderRequest(
    val products: List<Product>, val totalAmount: Double
)