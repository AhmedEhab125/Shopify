package com.example.shopify.personal.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.Models.draftOrderCreation.LineItem
import com.example.shopify.R
import com.example.shopify.databinding.FavCardBinding
import com.example.shopify.utiltes.Constants

class WishListAdapter (var wishList: List<LineItem>): RecyclerView.Adapter<WishListAdapter.WishViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val binding = FavCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishViewHolder(binding)
    }

    override fun getItemCount(): Int {
      if(wishList.size > 3) {
          return 2
      }
        return wishList.size-1
    }

    fun updateWishList(updatedWishList:List<LineItem>){
        wishList = updatedWishList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
            var favItem = wishList[position + 1]
            var id_imageUrl = (favItem.sku ?: ",").split(",")
            if (id_imageUrl.size == 2) {
                var id = id_imageUrl[0]
                var imageUrl = id_imageUrl[1]
                Glide.with(holder.binding.root).load(imageUrl).into(holder.binding.favImg)
            }
            holder.binding.favProductName.text = favItem.title
            holder.binding.favProductPrice.text = "${favItem.price?.toDouble()
                ?.times(Constants.currencyValue)} ${Constants.currencyType}"
            holder.binding.deleteBtn.visibility = View.GONE
        }


    inner class WishViewHolder(var binding: FavCardBinding) : RecyclerView.ViewHolder(binding.root)
}