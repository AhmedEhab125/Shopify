package com.example.shopify.repo

import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.createUserToSaveAtApi
import com.example.shopify.responseOfCreatingUser
import com.example.shopify.responseOfDraftCreation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRegisterUserInterFace : RegisterUserInterFace {
    override suspend fun createUserAtApi(user: CustomerRegistrationModel): Flow<CustomerRegistrationModel?> {
        return flowOf(responseOfCreatingUser())
    }

    override suspend fun createWishDraftOrder(draftOrder: DraftOrderPost): Flow<DraftOrderPost?> {
        return flowOf(responseOfDraftCreation())
    }
}