package com.example.shopify.products.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.databinding.BrandCardBinding

class ProductsAdapter (var products: List<String>): RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = BrandCardBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        holder.binding.brandImg.setImageResource(R.drawable.headphone)
        holder.binding.brandName.text = "1000$"
    }
    inner class ProductViewHolder(var binding: BrandCardBinding) : RecyclerView.ViewHolder(binding.root)
}