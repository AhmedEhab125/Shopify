package com.example.shopify.utiltes

import com.example.shopify.Models.postOrderModel.PostOrderModel
import com.example.shopify.Models.postOrderModel.ShippingAddress

object Constants {
    const val apiKey = "e52752c65c1032cbdc01f3dd6af2b01e"
    const val accesstoken = "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"
    const val secretKey = "6b4669b96493b0fda3430e65384fa92e"
    const val hostName = "mad43-alex-and-team1.myshopify.com/"
    const val currency = "currency"
    const val dollar = "Dollar"
    const val pound = "Pound"
    var currencyType = "$"
     var currencyValue = 1
    const val emailRegex = "[a-zA-Z0-9._-]+@(gmail|Gmail).com"
    const val publichKey = "pk_test_51NIMqzGJQLCGAlR3hbriza413hbhKXCnicHGGjBhIVFJEu6CTUJDsu3gb60etSlob5oUBEEyUu467FseXdeS4r7o00s5l2xyCn"
    const val paymentSecretKey = "sk_test_51NIMqzGJQLCGAlR3t6MABTFYQzydaXwDofKLrhKZjnaNPhadCi2ZfSthsVZRlz0R1jVkFh4KVQkDegBKDexc6Mva00druDPnMm"

    var selectedAddress : ShippingAddress? = null

}