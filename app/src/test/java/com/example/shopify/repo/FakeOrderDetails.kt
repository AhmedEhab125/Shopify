package com.example.shopify.repo

import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.orderDetails.viewModel.getOrderDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeOrderDetails:ISelectedProducts {
    override suspend fun getSelectedProducts(ids: String): Flow<CollectProductsModel> {
        return flowOf(getOrderDetails())
    }
}