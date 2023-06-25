package com.example.shopify.repo

import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.Models.productDetails.ProductModel
import kotlinx.coroutines.flow.Flow

interface IBrands {
    suspend fun getBrands() : Flow<BrandModel?>


}