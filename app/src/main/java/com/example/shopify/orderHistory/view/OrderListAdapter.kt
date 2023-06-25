package com.example.shopify.orderHistory.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.Models.orderList.Order
import com.example.shopify.R
import com.example.shopify.databinding.OrderItemBinding
import com.example.shopify.utiltes.Constants

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
        var date = list[position].created_at?.split("T")
        holder.binding.tvOrderPrice.text = (list[position].current_total_price?.toDouble()
            ?.times(Constants.currencyValue))?.toInt()?.plus(20).toString()+  " ${Constants.currencyType}"
        holder.binding.tvOrderTime.text = date?.get(0) ?: ""
        holder.binding.cvOrders.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("order", list[position])
            }
            Navigation.findNavController(it).navigate(R.id.action_orderListFragment_to_orderDetailsFragment,bundle)
        }


    }


    inner class ViewHolder(var binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root)
}