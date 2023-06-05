package com.example.shopify.cart.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.databinding.BrandCardBinding
import com.example.shopify.databinding.CartCardBinding

class CartAdapter(cartList: List<String>):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartCardBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return 10
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
       holder.binding.productImg.setImageResource(R.drawable.headphone)
        holder.binding.productName.text = "HeadPhone"
        holder.binding.productPrice.text = "120$"
    }
    inner class CartViewHolder(var binding: CartCardBinding): RecyclerView.ViewHolder(binding.root)
}