package com.example.shopify.signup.model

import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.repo.RegisterUserInterFace
import kotlinx.coroutines.flow.Flow

class ConcreteRegisterUser (var remoteSource : RegisterUserInterFace) : RegisterUserInterFace {
    override suspend fun createUserAtApi(user: CustomerRegistrationModel): Flow<CustomerRegistrationModel?> {
        return remoteSource.createUserAtApi(user)
    }
}