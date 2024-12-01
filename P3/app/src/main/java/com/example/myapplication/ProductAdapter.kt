package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(   private val productList: MutableList<Product>,
                        private val onDeleteClick: (Product) -> Unit ) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // Crea el ViewHolder y asocia el layout para cada elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }


    class ProductViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.
        findViewById(R.id.productName)
        val textViewPrice: TextView = itemView.
        findViewById(R.id.productPrice)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }
    override fun onBindViewHolder(holder:
                                  ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.textViewName.text = product.name
        holder.textViewPrice.text = "${product.price}€"
        // Configura el evento de clic para el botón de eliminar
        holder.deleteButton.setOnClickListener {
            onDeleteClick(product)
        }
    }

    // Devuelve el tamaño de la lista
    override fun getItemCount(): Int {
        return productList.size
    }

    // Método para eliminar un producto y notificar al RecyclerView
    fun removeProduct(product: Product) {
        val position = productList.indexOf(product)
        if (position != -1) {
            productList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
