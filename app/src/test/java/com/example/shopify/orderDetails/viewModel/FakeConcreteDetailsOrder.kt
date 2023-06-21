package com.example.shopify.orderDetails.viewModel

import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.repo.ISelectedProducts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeConcreteDetailsOrder:ISelectedProducts {
    override suspend fun getSelectedProducts(ids: String): Flow<CollectProductsModel?> {
        return flowOf(getOrderDetails())
    }
}