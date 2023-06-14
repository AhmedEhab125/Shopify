package com.example.shopify.orderHistory.model

import com.example.shopify.Models.orderList.RetriveOrderModel
import com.example.shopify.repo.IOrderList
import kotlinx.coroutines.flow.Flow

class OrderListRepo(var repo :IOrderList) : IOrderList {


    override suspend fun getOrderList(): Flow<RetriveOrderModel?> {
        return  repo.getOrderList()
    }
}