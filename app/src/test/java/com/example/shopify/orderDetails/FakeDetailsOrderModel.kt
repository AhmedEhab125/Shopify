package com.example.shopify.orderDetails.viewModel

import com.example.shopify.Models.productDetails.Product
import com.example.shopify.Models.products.CollectProductsModel

fun getOrderDetails():CollectProductsModel = CollectProductsModel(
    listOf(Product(null,null,null,null,8350702338370,
        null,null,null,null,
        null,null,null,null,
        null,"dummy product",null,null,null,false))
)