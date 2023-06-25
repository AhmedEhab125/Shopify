package com.example.shopify.repo

import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.addressPicker.viewModel.getResponseOfAddNewAddress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAddAddressInterface : IAddCustomerAddress {
    override suspend fun addAddress(
        customerId: Long,
        addresse: AddNewAddress
    ): Flow<AddressesModel?> {
        return flowOf(getResponseOfAddNewAddress())
    }
}