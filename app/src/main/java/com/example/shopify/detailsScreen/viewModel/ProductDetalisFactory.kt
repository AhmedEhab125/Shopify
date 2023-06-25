package com.example.shopify.detailsScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.home.viewModel.HomeViewModel
import com.example.shopify.repo.IBrands
import com.example.shopify.repo.ProductDetalisInterface

class ProductDetalisFactory (private val irepo: ProductDetalisInterface) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(ProductDetalisViewModel::class.java)) {

            ProductDetalisViewModel(irepo) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}