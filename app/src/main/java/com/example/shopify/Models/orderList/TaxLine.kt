package com.example.shopify.Models.orderList

data class TaxLine(
    val price: String,
    val price_set: PriceSet,
    val rate: Double,
    val title: String
)