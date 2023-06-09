package com.example.shopify.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.Models.brands.SmartCollection
import com.example.shopify.R
import com.example.shopify.databinding.BrandCardBinding
import com.example.shopify.databinding.ItemImgeBinding

class BrandsAdapter (var list: List<SmartCollection>): RecyclerView.Adapter<BrandsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BrandCardBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
       // return 10
    }
    fun setBrandsList(list: List<SmartCollection>){
        this.list= list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.brandImg.setImageResource(R.drawable.headphone)
        holder.binding.brandName.text = list[position].title
        Glide.with(holder.binding.root).load(list[position].image.src).into(holder.binding.brandImg)
        //holder.binding.brandName.text = list[position]
    }
    inner class ViewHolder(var binding: BrandCardBinding) : RecyclerView.ViewHolder(binding.root)
}