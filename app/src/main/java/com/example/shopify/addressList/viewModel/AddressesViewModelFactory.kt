package com.example.shopify.addressList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.repo.IAddresses

class AddressesViewModelFactory (private val iRepo: IAddresses) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(AddressesViewModel::class.java)) {

            AddressesViewModel(iRepo) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}