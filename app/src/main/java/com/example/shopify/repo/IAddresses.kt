package com.example.shopify.repo

import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.Models.registrashonModel.Addresse
import kotlinx.coroutines.flow.Flow

interface IAddresses {
    suspend fun getAddresses(customerId :Long) : Flow<AddressesModel?>
    suspend fun removeAddresses(customerId :Long , addressId :Long)
}