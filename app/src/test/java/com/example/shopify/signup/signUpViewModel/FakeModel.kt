package com.example.shopify

import com.example.shopify.Models.draftOrderCreation.DraftOrder
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.draftOrderCreation.LineItem
import com.example.shopify.Models.registrashonModel.Addresse
import com.example.shopify.Models.registrashonModel.Customer
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel

fun createUserToSaveAtApi(): CustomerRegistrationModel {
    val address = listOf<Addresse>(
        Addresse(
            null,
            address1 = "16,sidi beshr",
            "Alexandria",
            "Egypt",
            null,
            null,
            null,
            null,
            null
        )
    )
      val customer = Customer(
        null,
        addresses = address,
        "milad@gmail.com",
        "milad",
        "soliman",
        "123456789",
        "123456789",
        "01283606098",
        null,
        true
    )

    val user = CustomerRegistrationModel(customer)
    return user
}


fun responseOfCreatingUser() : CustomerRegistrationModel{
    val address = listOf<Addresse>(
        Addresse(
            null,
            address1 = "16,sidi beshr",
            "Alexandria",
            "Egypt",
            null,
            null,
            null,
            null,
            null
        )
    )
    val customer = Customer(
        6969454756162,
        addresses = address,
        "milad@gmail.com",
        "milad",
        "soliman",
        "123456789",
        "123456789",
        "01283606098",
        null,
        true
    )

    val user = CustomerRegistrationModel(customer)
    return user
}


fun creatDraftOrderToPostAtApi() : DraftOrderPost {

    var draftOrder = DraftOrder(
        null,
        null,
        listOf(
            LineItem(
                null,
                null,
                "Custome Item",
                "00.0",
                null,
                null,
                1,
                null,
                "Custom Item",
                null,
                null,
                null
            )
        ),
        "WishList",
        true,
        null
    )

    var draftOrderPost = DraftOrderPost(draftOrder)

    return draftOrderPost
}


fun responseOfDraftCreation() : DraftOrderPost {
    var draftOrder = DraftOrder(
        null,
        null,
        listOf(
            LineItem(
                null,
                null,
                "Custome Item",
                "00.0",
                null,
                null,
                1,
                null,
                "Custom Item",
                null,
                null,
                null
            )
        ),
        "WishList",
        true,
        6969454756162
    )

    var draftOrderPost = DraftOrderPost(draftOrder)

    return draftOrderPost
}

