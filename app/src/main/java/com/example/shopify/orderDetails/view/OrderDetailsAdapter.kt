package com.example.shopify.orderDetails.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.Models.orderList.LineItem
import com.example.shopify.Models.productDetails.Product
import com.example.shopify.databinding.OrderProductItemBinding
import com.example.shopify.utiltes.Constants

class OrderDetailsAdapter(
    var orderProductList: List<LineItem>,
    var productListFromApi: List<Product>
) :
    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            OrderProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderProductList.size
        // return 10
    }

    fun setProductList(list: List<LineItem>, productListFromApi: List<Product>) {
        this.orderProductList = list
        this.productListFromApi = productListFromApi
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvOrderProuductQuantity.text = "${orderProductList[position].quantity}"
        holder.binding.tvOrderProductName.text = orderProductList[position].name
        holder.binding.tvOrderProductPrice.text = "Price : ${orderProductList[position].price?.toDouble()
            ?.times(Constants.currencyValue)}  ${Constants.currencyType}"
        println(productListFromApi)
        if (orderProductList.size == productListFromApi.size)
            Glide.with(holder.binding.root).load(productListFromApi[position].image?.src)
                .into(holder.binding.ivOrderProduct)
    }

    inner class ViewHolder(var binding: OrderProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}