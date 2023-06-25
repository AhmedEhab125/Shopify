package com.example.shopify.addressList.view

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.constraintlayout.widget.Constraints
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.Models.postOrderModel.ShippingAddress
import com.example.shopify.Models.registrashonModel.Addresse
import com.example.shopify.R
import com.example.shopify.databinding.AdressessItemBinding
import com.example.shopify.utiltes.Constants
import com.example.shopify.utiltes.LoggedUserData

class AddressListAdapter(
    var list: MutableList<Addresse>,
    var iView: RemoveCustomerAddress,
    var comesFrom: String
) :
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

        if (list[position].default == true)
            holder.binding.ivDefulteAddress.visibility = View.VISIBLE
      //  holder.binding.imageView2.setImageResource(R.drawable.baseline_home_24)

        holder.binding.btnDeleteAddress.setOnClickListener {
            if (list[position].default == true) {
                cantDialog(holder.binding.root.context)

            } else
                deleteDialog(holder.binding.root.context, position)

        }
        holder.binding.cvAddress.setOnClickListener {
            if (comesFrom.equals("cart")) {
                Constants.selectedAddress = ShippingAddress(
                    address1 = list[position].address1!!,
                    city = list[position].city!!,
                    country = list[position].country!!,
                    first_name = list[position]?.first_name ?: "ahmed",
                    phone = list[position].phone ?: "010",
                    last_name = "ehab"
                )
                Navigation.findNavController(it)
                    .navigate(R.id.action_addressListFragment_to_paymentFragment)
            }
        }


    }

    fun deleteDialog(context: Context, position: Int) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_address_dialog)
        dialog.findViewById<Button>(R.id.delete).setOnClickListener {
            iView.remove(list[position].id!!)
            list.removeAt(position)
            notifyDataSetChanged()
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

    fun cantDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.cant_delete_address_dialog)

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


    inner class ViewHolder(var binding: AdressessItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}