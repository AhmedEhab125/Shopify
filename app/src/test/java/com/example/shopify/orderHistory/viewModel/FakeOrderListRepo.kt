package com.example.shopify.orderHistory.viewModel

import com.example.shopify.Models.orderList.RetriveOrderModel
import com.example.shopify.repo.IOrderList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeOrderListRepo : IOrderList {
    override suspend fun getOrderList(): Flow<RetriveOrderModel?> {
        return flowOf( getFakeOrderList())
    }
}