package com.example.shopify.repo

import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.Models.products.CollectProductsModel
import kotlinx.coroutines.flow.Flow

interface CollectionProductsInterface {
    suspend fun getCollectionProducts(id:Long) : Flow<CollectProductsModel?>
}