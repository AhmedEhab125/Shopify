package com.example.shopify.category.view

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.Models.draftOrderCreation.LineItem
import com.example.shopify.Models.productDetails.Product
import com.example.shopify.R
import com.example.shopify.databinding.CategoryItemBinding
import com.example.shopify.detailsScreen.view.ProductDetailsFragmentDirections
import com.example.shopify.utiltes.LoggedUserData
import com.google.firebase.auth.FirebaseAuth

class CategoryAdapter  (private var myProducts: List<Product> , val listener:OnClickToShowDetalisOfCategory) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    lateinit var binding: CategoryItemBinding
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding =CategoryItemBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    fun updateData(myNewProducts: List<Product>){
        this.myProducts = myNewProducts
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var product  = myProducts.get(position)

   if(FirebaseAuth.getInstance().currentUser!=null) {
    for (i in 1 until LoggedUserData.favOrderDraft.size) {
        var id_imageUrl = (LoggedUserData.favOrderDraft[i].sku ?: ",").split(",")
        if (id_imageUrl[0].equals(product.id.toString())) {
            holder.binding.categoryAddToFav.setBackgroundResource(R.drawable.favorite_clicked)
            product.isFav = true
            break
        } else {
            holder.binding.categoryAddToFav.setBackgroundResource(R.drawable.favourite_btn)
            product.isFav = false
        }
    }
  }


        holder.binding.categoryProudctSalary.text = product.title
        Glide.with(holder.binding.root).load(product.image?.src).into(holder.binding.ctegoryProductImgV)


        holder.binding.root.setOnClickListener {
            listener.showDetalisFromCategory(product.id ?: 8350702272834)
        }


        holder.binding.categoryAddToFav.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                if (!product.isFav) {
                    addToFav(product)
                    holder.binding.categoryAddToFav.setBackgroundResource(R.drawable.favorite_clicked)
                    product.isFav = true

                } else {
                    removeFromFav(product)
                    holder.binding.categoryAddToFav.setBackgroundResource(R.drawable.favourite_btn)
                    product.isFav = false
                }

            }else{
                navToLoginScreen(it)
            }
        }


    }

    private fun removeFromFav(product: Product) {
        val iterator = LoggedUserData.favOrderDraft.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.title == product.title) {
                iterator.remove()
            }
        }
          Toast.makeText(context, "Product Removed From Favorite List", Toast.LENGTH_SHORT).show()
    }

    private fun addToFav(product: Product) {
  if (isAleradyFav(product)){
  }else {
      val lineItem = LineItem(
          price = product.variants?.get(0)?.price,
          quantity = 1,
          sku = "${product.id},${product.image?.src}",
          title = product.title
      )
      Toast.makeText(context, "Product Added To Favorite List", Toast.LENGTH_SHORT).show()
      LoggedUserData.favOrderDraft.add(lineItem)
  }

    }

    private fun isAleradyFav(product: Product) : Boolean{
        LoggedUserData.favOrderDraft.forEach { item->
            if (item.title == product.title)
                return true
        }
        return false
    }



    override fun getItemCount(): Int {
        return myProducts.size

    }

    inner class ViewHolder(var binding:CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }


    private fun navToLoginScreen(view:View) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.goto_login_dialog)
        dialog.findViewById<Button>(R.id.ok).setOnClickListener {
          val action = CategoryFragmentDirections.fromCategoryToLogn("category",0)
           Navigation.findNavController(view).navigate(action)
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
        }
        val window: Window? = dialog.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }


}