package com.example.shopify.home.model

import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.repo.IBrands
import com.example.shopify.repo.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class HomeRepo(var remoteSource: IBrands) :IBrands {
    override suspend fun getBrands(): Flow<BrandModel?> {
        return remoteSource.getBrands()
    }
}