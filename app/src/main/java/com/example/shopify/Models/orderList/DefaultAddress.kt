package com.example.shopify.Models.orderList

data class DefaultAddress(
    val address1: String?,
    val city: String,
    val country: String,
    val country_code: String,
    val country_name: String?,
    val customer_id: Long,
    val default: Boolean,
    val first_name: String,
    val id: Long,
    val last_name: String,
    val name: String,
    val phone: String,
    val zip: String
)