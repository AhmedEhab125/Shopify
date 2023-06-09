package com.example.shopify.nework

import com.example.shopify.utiltes.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitShopifyHelper{
   const val BASE_URL = "https://${Constants.apiKey}:${Constants.accesstoken}${Constants.hostName}"
    val retrofitInstance = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}


