package com.example.shopify.category.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.home.viewModel.HomeViewModel
import com.example.shopify.repo.IAllProducts

class CategoryViewModelFactory (private val iRepo: IAllProducts) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {

            CategoryViewModel(iRepo) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}