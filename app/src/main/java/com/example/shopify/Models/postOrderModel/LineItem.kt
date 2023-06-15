package com.example.shopify.Models.postOrderModel

data class LineItem(

    val name: String,
    val product_id: Long,
    val quantity: Int,
    val sku: String,
    val variant_id: Long
)