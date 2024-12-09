package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private var cartItems: MutableList<Product>,
    private val removeCallback: (Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val productCantidad: TextView = view.findViewById(R.id.productCantidad)
        val productTotal: TextView = view.findViewById(R.id.productTotal)
        val buttonRemove: Button = view.findViewById(R.id.buttonRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.productcart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = cartItems[position]
        holder.productName.text = product.name
        holder.productPrice.text = "Precio: ${"%.2f".format(product.price)}€"
        holder.productCantidad.text = "Cantidad: ${product.num}"
        holder.productTotal.text = "Total: ${"%.2f".format(product.price * product.num)}€"
        holder.buttonRemove.setOnClickListener { removeCallback(product) }
    }

    override fun getItemCount() = cartItems.size

    fun updateCart(newCartItems: MutableList<Product>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }
}

