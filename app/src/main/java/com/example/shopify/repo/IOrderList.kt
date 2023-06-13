package com.example.shopify.repo

import com.example.shopify.Models.orderList.RetriveOrderModel
import kotlinx.coroutines.flow.Flow

interface IOrderList {
    suspend fun getOrderList(): Flow<RetriveOrderModel?>
}