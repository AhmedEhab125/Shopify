package com.example.shopify.cart.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.Models.draftOrderCreation.LineItem
import com.example.shopify.R
import com.example.shopify.cart.model.Communicator
import com.example.shopify.databinding.CartCardBinding
import com.example.shopify.utiltes.Constants

class CartAdapter(var cartList: List<LineItem>,val cartDelegate:Communicator) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cartList.size-1
    }

    fun updateCartList(list: List<LineItem>) {
        cartList = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
          var cartItem = cartList[position+1]
              var id_imageUrl = (cartItem.sku?:",").split(",")
              if(id_imageUrl.size == 3){
                  var id = id_imageUrl[0]
                  var imageUrl = id_imageUrl[2]
                  Log.i("essamImage", ""+imageUrl)
                  Glide.with(holder.binding.root).load(imageUrl).into(holder.binding.productImg)
              }

          holder.binding.productName.text = cartItem.title
          holder.binding.productPrice.text =" ${(cartItem.price?.toDouble()
              ?.times(Constants.currencyValue))?.toInt()} ${Constants.currencyType} "
          holder.binding.countLabel.text = cartItem.quantity.toString()
          holder.binding.productInc.setOnClickListener{
              cartDelegate.addItem(position+1,1)
          }
          holder.binding.productDec.setOnClickListener{
              cartDelegate.subItem(position+1,1)
          }

    }

    inner class CartViewHolder(var binding: CartCardBinding) : RecyclerView.ViewHolder(binding.root)
}