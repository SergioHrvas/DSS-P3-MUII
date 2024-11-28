package com.example.myapplication

data class CartProduct(
    val id: Long,
    val name: String,
    val price: Double,
    val cantidad: Int,
    val total: Double
) {
    // Constructor secundario que toma solo 'name', 'price' y 'cantidad'
    constructor(name: String, price: Double, cantidad: Int) : this(
        id = 0, // Valor predeterminado para 'id'
        name = name,
        price = price,
        cantidad = cantidad,
        total = price * cantidad // Calcula automáticamente el total
    )
}

