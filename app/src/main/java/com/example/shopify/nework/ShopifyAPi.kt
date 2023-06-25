package com.example.shopify.nework

object ShopifyAPi {
    val retrofitService: ShopifyApiService by lazy {
        RetrofitShopifyHelper.retrofitInstance.create(ShopifyApiService::class.java)
    }
}