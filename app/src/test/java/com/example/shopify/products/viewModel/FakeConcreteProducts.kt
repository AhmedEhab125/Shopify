package com.example.shopify.products.viewModel

import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.repo.CollectionProductsInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeConcreteProducts: CollectionProductsInterface {
    override suspend fun getCollectionProducts(id: Long): Flow<CollectProductsModel?> {
        return flowOf(responseOfBrandProducts())
    }
}