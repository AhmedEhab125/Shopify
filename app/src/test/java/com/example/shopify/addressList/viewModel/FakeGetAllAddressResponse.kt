package com.example.shopify.addressList.viewModel

import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.Models.registrashonModel.Addresse




fun getResponseOfAddNewAddress() : AddressesModel{
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
        ),
        Addresse(
            null,
            address1 = "25, Manshit Naser",
            "Cairo",
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