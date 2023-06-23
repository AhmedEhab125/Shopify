package com.example.shopify.repo

import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.home.viewModel.getFakeResponseOfSmartCollections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.math.floor

class FakeIBRands : IBrands {
    override suspend fun getBrands(): Flow<BrandModel?> {
        return flowOf(getFakeResponseOfSmartCollections())
    }
}