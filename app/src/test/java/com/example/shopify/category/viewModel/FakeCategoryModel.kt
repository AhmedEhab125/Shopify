package com.example.shopify.category.viewModel

import com.example.shopify.Models.productDetails.Product
import com.example.shopify.Models.products.CollectProductsModel

fun fakeResponseOfAllProductsModel(): CollectProductsModel = CollectProductsModel(
    listOf(
        Product(null,null,null,null,1010101010101010,
        null,null,null,null,
        null,null,null,null,
        null,"ProductOne",null,null,null,false),
        Product(null,null,null,null,12121212121212,
            null,null,null,null,
            null,null,null,null,
            null,"ProductTwo",null,null,null,false)
    )
)