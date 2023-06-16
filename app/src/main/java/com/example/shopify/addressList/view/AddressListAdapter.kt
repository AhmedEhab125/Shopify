package com.example.shopify.addressList.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.Models.registrashonModel.Addresse
import com.example.shopify.databinding.AdressessItemBinding

class AddressListAdapter(var list: MutableList<Addresse>, var iView :RemoveCustomerAddress) :
    RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdressessItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size

    }

    fun setAddressesList(list: MutableList<Addresse>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvAddress.text =
            "${list[position].address1}, ${list[position].city}, ${list[position].country}"
        holder.binding.btnDeleteAddress.setOnClickListener {
            iView.remove(list[position].id!!)
           list.removeAt(position)
            notifyDataSetChanged()

        }
    }

    inner class ViewHolder(var binding: AdressessItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}