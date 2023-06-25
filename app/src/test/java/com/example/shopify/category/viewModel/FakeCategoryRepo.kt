package com.example.shopify.category.viewModel

import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.repo.IAllProducts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCategoryRepo : IAllProducts {
    override suspend fun getAllProducts(): Flow<CollectProductsModel?> {
        return flowOf(fakeResponseOfAllProductsModel())
    }
}