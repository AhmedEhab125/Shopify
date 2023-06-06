package com.example.shopify.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.shopify.databinding.BrandCardBinding
import com.example.shopify.databinding.ItemImgeBinding

class AdsAdapter(var adsImges:ArrayList<Int>,var viewPager2: ViewPager2):RecyclerView.Adapter<AdsAdapter.AdsViewHolder>() {
    private val runnable = Runnable {
        adsImges.addAll(adsImges)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val binding = ItemImgeBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return AdsViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return adsImges.size
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        holder.adsBinding.productImg.setImageResource(adsImges[position])
       if (position == adsImges.size-1){
           viewPager2.post(runnable)
       }
    }
    inner class AdsViewHolder(var adsBinding: ItemImgeBinding) : RecyclerView.ViewHolder(adsBinding.root)
}