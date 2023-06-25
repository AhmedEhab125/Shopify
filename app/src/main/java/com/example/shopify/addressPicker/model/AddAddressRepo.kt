package com.example.shopify.addressPicker.model

import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addAddress.Address
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.repo.IAddCustomerAddress
import com.example.shopify.repo.IAddresses
import kotlinx.coroutines.flow.Flow

class AddAddressRepo (var remoteSource: IAddCustomerAddress): IAddCustomerAddress{
    override suspend fun addAddress(customerId: Long, addresse: AddNewAddress): Flow<AddressesModel?> {
        return remoteSource.addAddress(customerId,addresse)
    }
}