package com.example.shopify.repo

import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addAddress.Address
import com.example.shopify.Models.addressesmodel.AddressesModel
import kotlinx.coroutines.flow.Flow

interface IAddCustomerAddress {
    suspend fun addAddress(customerId:Long, addresse: AddNewAddress): Flow<AddressesModel?>
}