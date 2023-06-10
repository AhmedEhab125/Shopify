package com.example.shopify.repo

import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.nework.ShopifyApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RemoteSource(var network : ShopifyApiService) : IBrands, ProductDetalisInterface,CollectionProductsInterface,IAllProducts {
    override suspend fun getBrands(): Flow<BrandModel?> {
        return flowOf(network.getBrands().body())
    }

    override suspend fun getProductDetalis(): Flow<ProductModel?> {
        return flowOf(network.getProductDetails().body())
    }

    override suspend fun getCollectionProducts(id:Long): Flow<CollectProductsModel?> {
        return  flowOf(network.getCollectionProducts(id).body())
    }

    override suspend fun getAllProducts(): Flow<CollectProductsModel?> {
        return  flowOf(network.getAllProducts().body())
    }
}