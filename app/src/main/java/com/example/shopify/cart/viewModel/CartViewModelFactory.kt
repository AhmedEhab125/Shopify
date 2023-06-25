package com.example.shopify.cart.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.home.viewModel.HomeViewModel
import com.example.shopify.repo.IBrands
import com.example.shopify.repo.ICart

class CartViewModelFactory (private val irepo: ICart) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(CartViewModel::class.java)) {

            CartViewModel(irepo) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}