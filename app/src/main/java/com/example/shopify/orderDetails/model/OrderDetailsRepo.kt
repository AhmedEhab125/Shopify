package com.example.shopify.orderDetails.model

import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.repo.ISelectedProducts
import kotlinx.coroutines.flow.Flow

class OrderDetailsRepo(var remoteSource: ISelectedProducts) : ISelectedProducts {
    override suspend fun getSelectedProducts(ids: String): Flow<CollectProductsModel?> {
        return remoteSource.getSelectedProducts(ids)
    }
}