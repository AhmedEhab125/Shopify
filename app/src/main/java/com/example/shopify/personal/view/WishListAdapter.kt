package com.example.shopify.personal.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.databinding.FavCardBinding

class WishListAdapter (var wishList: List<String>): RecyclerView.Adapter<WishListAdapter.WishViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val binding = FavCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        holder.binding.favImg.setImageResource(R.drawable.headphone)
        holder.binding.favProductName.text = "HeadPhone"
        holder.binding.favProductPrice.text = "120$"
        holder.binding.deleteBtn.setImageResource(0)
    }

    inner class WishViewHolder(var binding: FavCardBinding) : RecyclerView.ViewHolder(binding.root)
}