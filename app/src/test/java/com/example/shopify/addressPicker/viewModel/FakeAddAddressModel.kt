package com.example.shopify.addressPicker.viewModel

import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addAddress.Address
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.Models.registrashonModel.Addresse

fun createAddressToadd() : AddNewAddress {
    var address = Address("16,Sidi Beshr Qebly","Alexandria","Egypt")

    val newAddress = AddNewAddress(address)

    return newAddress
}


fun getResponseOfAddNewAddress() : AddressesModel {
    val address = listOf<Addresse>(
        Addresse(
            null,
            address1 = "16,Sidi Beshr Qebly",
            "Alexandria",
            "Egypt",
            null,
            null,
            null,
            null,
            null
        )
    )

    val allAddress = AddressesModel(addresses = address)

    return allAddress
}