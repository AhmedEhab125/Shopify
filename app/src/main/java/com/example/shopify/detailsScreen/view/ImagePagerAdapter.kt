package com.example.shopify.detailsScreen.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.Models.productDetails.Image
import com.example.shopify.databinding.ItemImgeBinding

class ImagePagerAdapter(val context: Context, val imageIds: List<Image>?) :
    RecyclerView.Adapter<ImagePagerAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemImgeBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImgeBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var img = imageIds?.get(position)
        Glide.with(holder.binding.root).load(img?.src).into(holder.binding.productImg)
    }

    override fun getItemCount(): Int {
        return imageIds?.size ?: 1
    }
}