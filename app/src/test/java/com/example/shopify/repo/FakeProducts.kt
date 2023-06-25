package com.example.shopify.repo

import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.products.model.ICollectionProductsRepo
import com.example.shopify.products.viewModel.responseOfBrandProducts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeProducts:ICollectionProductsRepo {
    override suspend fun getCollectionProducts(id: Long): Flow<CollectProductsModel?> {
        return flowOf(responseOfBrandProducts())
    }

    override suspend fun getSelectedProducts(ids: String): Flow<CollectProductsModel?> {
        return flowOf(responseOfBrandProducts())
    }
}