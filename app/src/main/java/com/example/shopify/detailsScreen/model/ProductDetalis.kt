package com.example.shopify.detailsScreen.model

import com.example.shopify.Models.productDetails.ProductModel

import com.example.shopify.repo.ProductDetalisInterface
import kotlinx.coroutines.flow.Flow

class ConcreteProductDetalis(var remoteSource: ProductDetalisInterface) : ProductDetalisInterface {
    override suspend fun getProductDetalis(id:Long): Flow<ProductModel?> {
      return  remoteSource.getProductDetalis(id)
    }
}