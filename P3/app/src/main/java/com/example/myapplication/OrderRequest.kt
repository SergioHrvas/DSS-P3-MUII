package com.example.myapplication

data class OrderRequest(
    val products: MutableList<CartProduct>,
    val totalAmount: Double
) {
    data class OrderItemRequest(
        val productId: Long,
        val productName: String,
        val productPrice: Double,
        val quantity: Int
    )
}