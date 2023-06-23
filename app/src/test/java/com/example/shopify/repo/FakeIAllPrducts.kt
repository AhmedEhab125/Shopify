package com.example.shopify.repo

import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.category.viewModel.fakeResponseOfAllProductsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeIAllPrducts  : IAllProducts {
    override suspend fun getAllProducts(): Flow<CollectProductsModel?> {
        return flowOf(fakeResponseOfAllProductsModel())
    }
}