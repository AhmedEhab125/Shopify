package com.example.shopify.cart.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.R
import com.example.shopify.databinding.FragmentCartBinding

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
    }


}
