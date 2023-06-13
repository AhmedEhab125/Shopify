package com.example.shopify.orderDetails.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.Models.orderList.Order
import com.example.shopify.databinding.OrderItemBinding

class OrderListAdapter(var list: List<Order>) :
    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size

    }

    fun setOrderList(list: List<Order>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var date = list[position].created_at.split("T")
        holder.binding.tvOrderPrice.text = list[position].current_total_price
        holder.binding.tvOrderTime.text = date[0]


    }


    inner class ViewHolder(var binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root)
}