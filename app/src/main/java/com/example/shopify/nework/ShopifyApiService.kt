package com.example.shopify.nework

import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.Models.collects.CollectModel
import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.utiltes.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ShopifyApiService {

    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @GET("admin/api/2023-04/collects.json")
    suspend fun getCollects() : Response<CollectModel>
    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @GET("admin/api/2023-04/smart_collections.json")
    suspend fun getBrands() : Response<BrandModel>

    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @GET("admin/api/2023-04/products/8350702272834.json")
    suspend fun getProductDetails() : Response<ProductModel>

}