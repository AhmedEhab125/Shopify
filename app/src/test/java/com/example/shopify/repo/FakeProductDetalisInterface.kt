package com.example.shopify.repo

import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.detailsScreen.viewModel.getProductDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeProductDetalisInterface : ProductDetalisInterface {
    override suspend fun getProductDetalis(id: Long): Flow<ProductModel?> {
        return flowOf(getProductDetails())
    }
}