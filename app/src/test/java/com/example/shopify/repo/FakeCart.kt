package com.example.shopify.repo

import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.cart.viewModel.getCartData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCart:ICart {
    override suspend fun getCartItems(draftId: Long): Flow<DraftOrderPost?> {
        return flowOf(getCartData())
    }


    override suspend fun upDateCartOrderDraft(draftID: Long, draftOrder: DraftOrderPost) {
        TODO("Not yet implemented")
    }
}