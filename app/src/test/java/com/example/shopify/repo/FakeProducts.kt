package com.example.shopify.repo

import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.products.viewModel.responseOfBrandProducts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeProducts:CollectionProductsInterface {
    override suspend fun getCollectionProducts(id: Long): Flow<CollectProductsModel?> {
        return flowOf(responseOfBrandProducts())
    }
}