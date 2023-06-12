package com.example.shopify.addressPicker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.repo.IAddCustomerAddress

class AddAddressViewModelFactory (private val iRepo: IAddCustomerAddress) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(AddAddressViewModel::class.java)) {

            AddAddressViewModel(iRepo) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}