package com.example.shopify.personal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.databinding.FragmentPersonalBinding


class PersonalFragment : Fragment() {
    private lateinit var personalBinding: FragmentPersonalBinding
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var wishListAdapter: WishListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personalBinding = FragmentPersonalBinding.inflate(inflater)
        ordersAdapter = OrdersAdapter(listOf())
        wishListAdapter = WishListAdapter(listOf())
        return personalBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personalBinding.orderRV.adapter = ordersAdapter
        personalBinding.orderRV.layoutManager = LinearLayoutManager(requireContext())
        personalBinding.whishListRV.adapter = wishListAdapter
        personalBinding.whishListRV.layoutManager = LinearLayoutManager(requireContext())
        personalBinding.orderMore.setOnClickListener {
            print("more of orders")
        }
        personalBinding.whishListMore.setOnClickListener {
            print("more of WishList")
        }
    }


}