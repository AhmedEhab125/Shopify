package com.example.shopify.detailsScreen.viewModel

import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.repo.ProductDetalisInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeConcreteProductDetalis : ProductDetalisInterface {
    override suspend fun getProductDetalis(id: Long): Flow<ProductModel?> {
        return flowOf(getProductDetails())
    }
}