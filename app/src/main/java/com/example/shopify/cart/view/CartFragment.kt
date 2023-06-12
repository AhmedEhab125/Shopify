package com.example.shopify.cart.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.R
import com.example.shopify.databinding.FragmentCartBinding
import com.example.shopify.mainActivity.MainActivity

class CartFragment : Fragment() {
    private lateinit var cartBinding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cartBinding=FragmentCartBinding.inflate(inflater)
        cartAdapter = CartAdapter(listOf())
        return cartBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartBinding.cartRV.adapter=cartAdapter
        cartBinding.cartRV.layoutManager=LinearLayoutManager(requireContext())
        cartBinding.totalPrice.text="1000$"

        cartBinding.checkoutBtn.setOnClickListener {
        //    Navigation.findNavController(requireView()).navigate(R.id.from_cart_to_login)
        }


    }
    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
    }


}
