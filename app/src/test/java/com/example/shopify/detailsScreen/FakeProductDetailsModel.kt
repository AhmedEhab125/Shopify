package com.example.shopify.detailsScreen.viewModel

import com.example.shopify.Models.productDetails.Product
import com.example.shopify.Models.productDetails.ProductModel


fun getProductDetails() : ProductModel {
     var product = Product(null,null,null,null,
          1116406546754,null, listOf(),null,null,null,null
          ,null,null,null,null,null,null,null,true)

     val productModel =ProductModel(product)

     return productModel
}