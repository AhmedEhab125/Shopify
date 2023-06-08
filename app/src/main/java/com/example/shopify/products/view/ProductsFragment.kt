package com.example.shopify.products.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopify.R
import com.example.shopify.databinding.FragmentProductsBinding


class ProductsFragment : Fragment() {
  private lateinit var productsBinding: FragmentProductsBinding
  private lateinit var productsAdapter: ProductsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productsBinding = FragmentProductsBinding.inflate(inflater)
        productsAdapter = ProductsAdapter(listOf())
        return productsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productsBinding.productsRV.adapter = productsAdapter
        productsBinding.productsRV.layoutManager = GridLayoutManager(requireContext(),3)
    }


}