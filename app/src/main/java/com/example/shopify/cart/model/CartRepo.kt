package com.example.shopify.cart.model

import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.repo.ICart
import kotlinx.coroutines.flow.Flow

class CartRepo(var remoteSource: ICart) : ICart {
    override suspend fun getCartItems(draftId: Long): Flow<DraftOrderPost?> {
        return remoteSource.getCartItems(draftId)
    }

    override suspend fun upDateCartOrderDraft(draftID: Long, draftOrder: DraftOrderPost) {
       remoteSource.upDateCartOrderDraft(draftID,draftOrder)
    }
}