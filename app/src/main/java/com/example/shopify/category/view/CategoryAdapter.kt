package com.example.shopify.category.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import android.os.RemoteException
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.CategoryItemBinding
import java.io.IOException
import java.util.*

class CategoryAdapter  (private var myProducts: List<FakeCategoryModel>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    lateinit var binding: CategoryItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding =CategoryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    fun updateData(myNewProducts:List<FakeCategoryModel>){
        this.myProducts = myNewProducts
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var product : FakeCategoryModel = myProducts.get(position)

        holder.binding.categoryProudctSalary.text = product.salary
        holder.binding.ctegoryProductImgV.setImageResource(product.img)
        holder.binding.categoryAddToFav.setOnClickListener {
            Log.i("Fav",product.salary)
        }


    }

    override fun getItemCount(): Int {
        return myProducts.size
    }

    inner class ViewHolder(var binding:CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }


}