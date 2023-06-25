package com.example.shopify.addressPicker.viewModel

import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.repo.IAddCustomerAddress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAddAddressRepo : IAddCustomerAddress {
    override suspend fun addAddress(
        customerId: Long,
        addresse: AddNewAddress
    ): Flow<AddressesModel?> {
        return flowOf(getResponseOfAddNewAddress())
    }
}