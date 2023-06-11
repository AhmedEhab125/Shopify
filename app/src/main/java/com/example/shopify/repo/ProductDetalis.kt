package com.example.shopify.repo

import com.example.shopify.Models.productDetails.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductDetalisInterface  {

    suspend fun getProductDetalis(id:Long) : Flow<ProductModel?>

}