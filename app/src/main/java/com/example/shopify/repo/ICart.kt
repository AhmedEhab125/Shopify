package com.example.shopify.repo

import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import kotlinx.coroutines.flow.Flow

interface ICart {
   suspend fun getCartItems(draftId:Long): Flow<DraftOrderPost?>
   suspend fun upDateCartOrderDraft(draftID:Long,draftOrder: DraftOrderPost)
}