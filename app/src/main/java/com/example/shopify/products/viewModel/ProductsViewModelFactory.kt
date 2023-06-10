package com.example.shopify.products.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.home.viewModel.HomeViewModel
import com.example.shopify.repo.CollectionProductsInterface
import com.example.shopify.repo.IBrands

class ProductsViewModelFactory (private val irepo: CollectionProductsInterface) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {

            ProductsViewModel(irepo) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}