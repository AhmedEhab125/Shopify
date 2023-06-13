package com.example.shopify.orderDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.home.viewModel.HomeViewModel
import com.example.shopify.repo.IBrands
import com.example.shopify.repo.IOrderList

class OrderListViewModelFactory (private val irepo: IOrderList) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(OrderListViewModel::class.java)) {

            OrderListViewModel(irepo) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}