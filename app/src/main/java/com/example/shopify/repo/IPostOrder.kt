package com.example.shopify.repo

import com.example.shopify.Models.RetriveOreder.RetriveOrder
import com.example.shopify.Models.postOrderModel.PostOrderModel
import kotlinx.coroutines.flow.Flow

interface IPostOrder {
    suspend fun createOrder(order : PostOrderModel): Flow<RetriveOrder?>
}