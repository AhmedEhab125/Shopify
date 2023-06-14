package com.example.shopify.Models.orderList

data class SmsMarketingConsent(
    val consent_collected_from: String,
    val opt_in_level: String,
    val state: String
)