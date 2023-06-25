package com.example.shopify.Models.postOrderModel

import com.example.shopify.Models.orderList.BillingAddress

data class Order(
    //val billing_address: BillingAddress
    var tags: String = "Cash",
    val currency: String,
    val current_total_price: String,
    val customer: Customer,
    val line_items: List<LineItem>,
    val shipping_address: ShippingAddress,
    val total_discounts: String
):java.io.Serializable