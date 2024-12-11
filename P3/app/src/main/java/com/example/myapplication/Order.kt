package com.example.myapplication

data class OrderRequest(
    val products: List<Product>,
    val totalAmount: Double
)

data class OrderResponse(
    val success: Boolean,
    val orderId: Int?,
    val message: String?
)