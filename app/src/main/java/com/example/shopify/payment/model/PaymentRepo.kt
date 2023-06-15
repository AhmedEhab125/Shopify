package com.example.shopify.payment.model

import com.example.shopify.Models.RetriveOreder.RetriveOrder
import com.example.shopify.Models.postOrderModel.PostOrderModel
import com.example.shopify.repo.IAddresses
import com.example.shopify.repo.IPostOrder
import kotlinx.coroutines.flow.Flow

class PaymentRepo(var remoteSource: IPostOrder):IPostOrder {
    override suspend fun createOrder(order: PostOrderModel): Flow<RetriveOrder?> {
        return remoteSource.createOrder(order)
    }
}