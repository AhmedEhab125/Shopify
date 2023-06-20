package com.example.shopify.favourite.favViewModel

import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.repo.ICart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeConcreteFavClass: ICart {
    override suspend fun getCartItems(draftId: Long): Flow<DraftOrderPost?> {
      return flowOf(getFakeFavoriteDraftOrde())
    }

    override suspend fun upDateCartOrderDraft(draftID: Long, draftOrder: DraftOrderPost) {
        TODO("Not yet implemented")
    }
}