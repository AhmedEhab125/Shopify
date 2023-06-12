package com.example.shopify.Models.registrashonModel

data class Customer(
    val addresses: List<Addresse>,
    val email: String,
    val first_name: String,
    val last_name: String,
    val password: String,
    val password_confirmation: String,
    val phone: String,
    val send_email_welcome: Boolean,
    val verified_email: Boolean
)