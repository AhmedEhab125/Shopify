package com.example.shopify.addressList.viewModel

import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.repo.IAddresses
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAddressesRepo : IAddresses {
    override suspend fun getAddresses(customerId: Long): Flow<AddressesModel?> {
      return flowOf(getResponseOfAddNewAddress())
    }

    override suspend fun removeAddresses(customerId: Long, addressId: Long) {
        TODO("Not yet implemented")
    }
}