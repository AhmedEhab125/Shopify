package com.example.shopify.products.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.Models.productDetails.Product
import com.example.shopify.databinding.BrandCardBinding
import com.example.shopify.databinding.BrandProductItemBinding
import com.example.shopify.utiltes.Constants

class ProductsAdapter (var products: List<Product> , var listener : OnClickToShowDetails): RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = BrandProductItemBinding.inflate( LayoutInflater.from(parent.context),parent,false)
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
        Glide.with(holder.binding.root).load(productsModel.image?.src).into(holder.binding.brandImg)
        holder.binding.brandName.text = productsModel.title
        holder.binding.tvBrandProductPrice.text = "${productsModel.variants?.get(0)?.price?.toDouble()?.times(Constants.currencyValue)?.toInt()} ${Constants.currencyType}"
        holder.binding.root.setOnClickListener {
            listener.ShowProductDetalis(productsModel.id ?: 8350702272834)
        }
    }
    inner class ProductViewHolder(var binding: BrandProductItemBinding) : RecyclerView.ViewHolder(binding.root)
}