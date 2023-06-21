package com.example.shopify.products.viewModel

import com.example.shopify.Models.productDetails.Product
import com.example.shopify.Models.products.CollectProductsModel
import kotlinx.coroutines.flow.Flow

fun responseOfBrandProducts():CollectProductsModel = CollectProductsModel(
    listOf(Product(null,null,null,null,1010101010101010,
        null,null,null,null,
        null,null,null,null,
        null,"dummy product",null,null,null,false))
)