package com.example.myapplication

data class Product(
    val id: Long,
    val name: String,
    val price: Double
) {
    // Secondary constructor that only takes 'name' and 'price'
    constructor(name: String, price: Double) : this(0, name, price) {
        // Default 'id' value set to 0, which can be used for new products
    }
}
