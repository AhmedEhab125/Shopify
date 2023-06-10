package com.example.shopify.repo

import com.example.shopify.Models.products.CollectProductsModel
import kotlinx.coroutines.flow.Flow

interface IAllProducts {
    suspend fun getAllProducts(): Flow<CollectProductsModel?>
}