package com.example.shopify.personal.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.ItemImgeBinding
import com.example.shopify.databinding.OrderCardBinding
import java.time.LocalDate
import java.util.*

class OrdersAdapter(var orderList: List<String>):RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = OrderCardBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.orderBinding.orderPrice.text = "5000$"
        holder.orderBinding.orderCreated.text = LocalDate.now().toString()
    }
    inner class OrderViewHolder(var orderBinding: OrderCardBinding) : RecyclerView.ViewHolder(orderBinding.root)
}