package com.example.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(
    private val context: Context,
    private val productList: MutableList<Product>,
    private val onDeleteClick: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.productName)
        val textViewPrice: TextView = itemView.findViewById(R.id.productPrice)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val addCartButton: Button = itemView.findViewById(R.id.addCartButton) // Asegúrate de tener este botón en el layout
        val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.textViewName.text = product.name
        holder.textViewPrice.text = "${product.price}€"

        val fullImageUrl = Constants.SERVER_URL + product.imagePath
        println(fullImageUrl)
        Glide.with(context)
            .load(fullImageUrl)
            .error(R.drawable.image_default_foreground)
            .into(holder.imageViewProduct)

        holder.deleteButton.setOnClickListener {
            onDeleteClick(product)
        }

        holder.addCartButton.setOnClickListener {
            onAddToCartClick(product)
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun removeProduct(product: Product) {
        val position = productList.indexOf(product)
        if (position != -1) {
            productList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}