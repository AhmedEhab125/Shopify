package com.example.shopify.category.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.Models.productDetails.Product
import com.example.shopify.databinding.CategoryItemBinding

class CategoryAdapter  (private var myProducts: List<Product>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    lateinit var binding: CategoryItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding =CategoryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    fun updateData(myNewProducts: List<Product>){
        this.myProducts = myNewProducts
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var product  = myProducts.get(position)

        holder.binding.categoryProudctSalary.text = product.title
        Glide.with(holder.binding.root).load(product.image?.src).into(holder.binding.ctegoryProductImgV)

        holder.binding.categoryAddToFav.setOnClickListener {
            product.title?.let { it1 -> Log.i("Fav", it1) }
        }


    }

    override fun getItemCount(): Int {
        return myProducts.size
    }

    inner class ViewHolder(var binding:CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }


}