package com.example.shopify.home.viewModel

import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.repo.IBrands
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeBrandsRepo:IBrands {
    override suspend fun getBrands(): Flow<BrandModel?> {
        return flowOf(getFakeBrands())
    }
}