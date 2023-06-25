package com.example.shopify.addressList.model

import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.Models.registrashonModel.Addresse
import com.example.shopify.repo.IAddresses
import com.example.shopify.repo.RemoteSource
import kotlinx.coroutines.flow.Flow

class AddressesRepo(var remoteSource: IAddresses): IAddresses {
    override suspend fun getAddresses(customerId: Long): Flow<AddressesModel?> {
      return remoteSource.getAddresses(customerId)
    }

    override suspend fun removeAddresses(customerId: Long, addressId: Long) {
        remoteSource.removeAddresses(customerId, addressId)
    }
}