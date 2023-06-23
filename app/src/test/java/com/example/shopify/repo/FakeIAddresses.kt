package com.example.shopify.repo

import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.addressList.viewModel.getResponseOfAddNewAddress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeIAddresses : IAddresses {
    override suspend fun getAddresses(customerId: Long): Flow<AddressesModel?> {
        return flowOf(getResponseOfAddNewAddress())
    }

    override suspend fun removeAddresses(customerId: Long, addressId: Long) {
        TODO("Not yet implemented")
    }
}