package com.example.shopify.Models.draftOrderCreation

data class DraftOrder(
    val applied_discount: AppliedDiscount?,
    val customer: Customer?,
    var line_items: List<LineItem>?,
    val note: String?,
    val use_customer_default_address: Boolean?,
    val id: Long?,
)