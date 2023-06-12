package com.example.shopify.repo

import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import kotlinx.coroutines.flow.Flow

interface RegisterUserInterFace {

    suspend fun createUserAtApi(user:CustomerRegistrationModel) : Flow<CustomerRegistrationModel?>
}