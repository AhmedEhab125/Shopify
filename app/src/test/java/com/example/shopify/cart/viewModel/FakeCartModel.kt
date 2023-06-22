package com.example.shopify.cart.viewModel

import com.example.shopify.Models.draftOrderCreation.DraftOrder
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.draftOrderCreation.LineItem

fun getCartData()=DraftOrderPost(
    DraftOrder(
    null, null,
    listOf(
        LineItem(
            null, null, "Custome Item", "00.0", null,
            null, 1, null, "Custom Item",
            null, null, null
        )
    ),
    "CartList",
    true,
    6969454756162
))
