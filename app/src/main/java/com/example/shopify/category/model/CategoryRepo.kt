package com.example.shopify.category.model

import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.repo.IAllProducts
import com.example.shopify.repo.IBrands
import kotlinx.coroutines.flow.Flow

class CategoryRepo(var remoteSource: IAllProducts) : IAllProducts {
    override suspend fun getAllProducts(): Flow<CollectProductsModel?> {
        return remoteSource.getAllProducts()
    }
}