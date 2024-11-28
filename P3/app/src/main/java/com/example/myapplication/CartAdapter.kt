package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(private val cartProductList: List<CartProduct>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // Crea el ViewHolder y asocia el layout para cada elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.productcart_item, parent, false)
        return CartViewHolder(view)
    }

    // Clase ViewHolder para los elementos del carrito
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.productName)
        val textViewPrice: TextView = itemView.findViewById(R.id.productPrice)
        val textViewCantidad: TextView = itemView.findViewById(R.id.productCantidad)
        val textViewTotal: TextView = itemView.findViewById(R.id.productTotal)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartProduct = cartProductList[position]

        // Asignar valores a las vistas
        holder.textViewName.text = cartProduct.name
        holder.textViewPrice.text = "Precio: ${cartProduct.price}€"
        holder.textViewCantidad.text = "Cantidad: ${cartProduct.cantidad}"
        holder.textViewTotal.text = "Total: ${cartProduct.total}€"
    }

    // Devuelve el tamaño de la lista
    override fun getItemCount(): Int {
        return cartProductList.size
    }
}
