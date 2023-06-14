package com.example.shopify.repo

import com.example.shopify.Models.products.CollectProductsModel
import kotlinx.coroutines.flow.Flow

interface ISelectedProducts {
    suspend fun getSelectedProducts(ids :String): Flow<CollectProductsModel?>

}