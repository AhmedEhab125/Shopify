package com.example.shopify.Models.draftOrderCreation

data class LineItem(
    val applied_discount: Any?,
    val custom: Boolean?,
    val name: String?,
    val price: String?,
    val product_id: Any?,
    val properties: List<Any>?,
    var quantity: Int?,
    val sku: Any?,
    val title: String?,
    val variant_id: Any?,
    val variant_title: Any?,
    val vendor: Any?
)