package com.example.shopify.personal.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.Models.orderList.Order
import com.example.shopify.R
import com.example.shopify.databinding.ItemImgeBinding
import com.example.shopify.databinding.OrderCardBinding
import com.example.shopify.databinding.OrderItemBinding
import com.example.shopify.orderHistory.view.OrderListAdapter
import com.example.shopify.utiltes.Constants
import java.time.LocalDate
import java.util.*

class OrdersAdapter(var list: List<Order>) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (list.size < 3) return list.size
        else return 2

    }

    fun setOrderList(list: List<Order>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var date = list[position].created_at?.split("T")
        holder.binding.orderPrice.text = (list[position].current_total_price?.toDouble()
            ?.times(Constants.currencyValue))?.toInt()?.plus(20).toString() + " ${Constants.currencyType}"
        holder.binding.orderCreated.text = date?.get(0) ?: ""

        /*   holder.binding..setOnClickListener {
               val bundle = Bundle().apply {
                   putSerializable("order", list[position])
               }
               Navigation.findNavController(it).navigate(R.id.action_orderListFragment_to_orderDetailsFragment,bundle)
           }
           */


    }


    inner class ViewHolder(var binding: OrderCardBinding) : RecyclerView.ViewHolder(binding.root)
}

