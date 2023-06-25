package com.example.shopify.orderDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.orderHistory.viewModel.OrderListViewModel
import com.example.shopify.repo.IOrderList
import com.example.shopify.repo.ISelectedProducts

class OrderDetailsViewModelFactory (private val irepo: ISelectedProducts) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(OrderDetailsViewModel::class.java)) {

            OrderDetailsViewModel(irepo) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}