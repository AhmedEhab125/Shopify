package com.example.shopify.favourite.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.databinding.FavCardBinding

class FavAdapter (var favList: List<String>):RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = FavCardBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return FavViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.binding.favImg.setImageResource(R.drawable.headphone)
        holder.binding.favProductName.text = "HeadPhone"
        holder.binding.favProductPrice.text = "120$"
    }
    inner class FavViewHolder(var binding: FavCardBinding): RecyclerView.ViewHolder(binding.root)
}