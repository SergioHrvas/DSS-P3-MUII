package com.example.myapplication

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.truncate

data class CartProduct(
    val id: Long,
    val name: String,
    val price: Double,
    val image: String,
    val num: Int,
) {
    val total: Double
        get() = BigDecimal(price * num).setScale(3, RoundingMode.HALF_EVEN).toDouble()

    // Constructor secundario que toma solo 'name', 'price' y 'cantidad'
    constructor(name: String, price: Double, num: Int) : this(
        id = 0, // Valor predeterminado para 'id'
        name = name,
        price = price,
        num = num,
        image = "default.png"
    )
}

