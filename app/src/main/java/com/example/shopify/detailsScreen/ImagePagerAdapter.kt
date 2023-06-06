package com.example.shopify.detailsScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.ItemImgeBinding

class ImagePagerAdapter(val context: Context, val imageIds: IntArray) :
    RecyclerView.Adapter<ImagePagerAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemImgeBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImgeBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.productImg.setImageResource(imageIds[position])
    }

    override fun getItemCount(): Int {
        return imageIds.size
    }
}