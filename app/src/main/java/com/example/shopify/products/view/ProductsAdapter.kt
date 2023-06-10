package com.example.shopify.products.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.Models.productDetails.Product
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.R
import com.example.shopify.databinding.BrandCardBinding

class ProductsAdapter (var products: List<Product>): RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = BrandCardBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    fun updateList(updatedList:List<Product>){
        products = updatedList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        var productsModel = products[position]
        Log.i("essam image", ""+productsModel.image)
        Glide.with(holder.binding.root).load(productsModel.image?.src).into(holder.binding.brandImg)
        holder.binding.brandName.text = productsModel.title
    }
    inner class ProductViewHolder(var binding: BrandCardBinding) : RecyclerView.ViewHolder(binding.root)
}