package com.example.shopify.Models.registrashonModel

data class Addresse(
    val id : Long?,
    val address1: String?,
    val city: String?,
    val country: String?,
    val first_name: String?,
    val last_name: String?,
    val phone: String?,
    val province: String?,
    val zip: String?,
    var default : Boolean? = null
)