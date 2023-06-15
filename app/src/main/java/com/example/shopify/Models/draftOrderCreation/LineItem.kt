package com.example.shopify.Models.draftOrderCreation

data class LineItem(
    val applied_discount: Any?=null,
    val custom: Boolean?=null,
    val name: String?=null,
    val price: String?,
    val product_id: Long?=null,
    val properties: List<Any>?=null,
    var quantity: Int?,
    val sku: String?,
    val title: String?,
    val variant_id: Long?=null,
    val variant_title: Any?=null,
    val vendor: Any?=null
)