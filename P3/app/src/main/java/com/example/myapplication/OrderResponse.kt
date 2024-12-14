package com.example.myapplication

data class OrderResponse(
    val id: Long,
    val totalAmount: Double,
    val status: String,
    val message: String? // Opcional para mensajes personalizados
)