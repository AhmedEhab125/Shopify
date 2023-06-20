package com.example.shopify.favourite.favViewModel

import com.example.shopify.Models.draftOrderCreation.DraftOrder
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.draftOrderCreation.LineItem


fun getFakeFavoriteDraftOrde() : DraftOrderPost {
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

    var favOrderPost = DraftOrderPost(draftOrder)

    return favOrderPost
}
