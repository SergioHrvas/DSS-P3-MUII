package com.example.myapplication

data class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val imagePath: String,
    //val num: Long =0
) {
    // Secondary constructor that only takes 'name' and 'price'
    constructor(name: String, price: Double, imagePath: String) : this(0, name, price, imagePath) {
        // Default 'id' value set to 0, which can be used for new products
    }
}
