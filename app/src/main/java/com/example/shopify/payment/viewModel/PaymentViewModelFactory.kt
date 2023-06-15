package com.example.shopify.payment.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.repo.IPostOrder

class PaymentViewModelFactory (private val iRepo: IPostOrder) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {

            PaymentViewModel(iRepo) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}