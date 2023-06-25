package com.example.shopify.repo

import com.example.shopify.Models.orderList.RetriveOrderModel
import com.example.shopify.orderHistory.viewModel.getFakeOrderList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeOrderHistory: IOrderList {
    override suspend fun getOrderList(): Flow<RetriveOrderModel?> {
        return flowOf( getFakeOrderList())
    }
}