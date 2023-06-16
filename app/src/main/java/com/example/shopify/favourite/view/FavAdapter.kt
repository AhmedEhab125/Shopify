package com.example.shopify.favourite.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.Models.draftOrderCreation.LineItem
import com.example.shopify.R
import com.example.shopify.databinding.FavCardBinding
import com.example.shopify.favourite.model.OnDelete

class FavAdapter (var favList: List<LineItem> , var listener : OnDelete):RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = FavCardBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return FavViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favList.size-1
    }

    fun updateFavList(favListUpdated : List<LineItem>){
        favList = favListUpdated
        notifyDataSetChanged()
        Log.i("ListAdapter",""+favList.size)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        var favItem = favList[position+1]

        var id_imageUrl = (favItem.sku?:",").split(",")
        if(id_imageUrl.size == 2){
            var id = id_imageUrl[0]
            var imageUrl = id_imageUrl[1]
            Glide.with(holder.binding.root).load(imageUrl).into(holder.binding.favImg)
        }
        holder.binding.favProductName.text = favItem.title
        holder.binding.favProductPrice.text = favItem.price
        holder.binding.deleteBtn.setOnClickListener {
            listener.deleteFromFav(position+1)
        }
    }
    inner class FavViewHolder(var binding: FavCardBinding): RecyclerView.ViewHolder(binding.root)
}
