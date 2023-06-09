package com.example.shopify.repo

import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.nework.ShopifyApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RemoteSource(var network : ShopifyApiService) : IBrands {
    override suspend fun getBrands(): Flow<BrandModel?> {
        return flowOf(network.getBrands().body())
    }
}