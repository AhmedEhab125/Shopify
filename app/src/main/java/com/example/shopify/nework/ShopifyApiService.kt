package com.example.shopify.nework

import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.Models.collects.CollectModel
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.orderList.RetriveOrderModel
import com.example.shopify.Models.postOrderModel.PostOrderModel
import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.utiltes.Constants
import retrofit2.Response
import retrofit2.http.*

interface ShopifyApiService {

    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @GET("admin/api/2023-04/collects.json")
    suspend fun getCollects() : Response<CollectModel>
    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @GET("admin/api/2023-04/smart_collections.json")
    suspend fun getBrands() : Response<BrandModel>

    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @GET("admin/api/2023-04/products/{id}.json")
    suspend fun getProductDetails(@Path(value = "id") id: Long) : Response<ProductModel>

    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @GET("admin/api/2023-04/collections/{id}/products.json")
    suspend fun getCollectionProducts(@Path(value = "id") id:Long) : Response<CollectProductsModel>
    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @GET("admin/api/2023-04/products.json")
    suspend fun getAllProducts() : Response<CollectProductsModel>
    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @POST("admin/api/2023-04/customers.json")
    suspend fun registerUserAtApi(@Body user : CustomerRegistrationModel) : Response<CustomerRegistrationModel>
    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @GET("admin/api/2023-04/customers/{customerId}/addresses.json")
    suspend fun getCustomerAddresses(@Path(value = "customerId") id:Long) : Response<AddressesModel>
    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @POST("admin/api/2023-04/customers/{customerId}/addresses.json")
    suspend fun addCustomerAddresse(@Path(value = "customerId") id:Long,@Body addresse: AddNewAddress) : Response<AddressesModel>

    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @GET("admin/api/2023-04/orders.json")
    suspend fun getAllOrders() : Response<RetriveOrderModel>
    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @GET("admin/api/2023-04/products.json")
    suspend fun getSelectedProductsDetails(@Query("ids") id: String) : Response<CollectProductsModel>

    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @POST("admin/api/2023-04/draft_orders.json")
    suspend fun createDraftOrder(@Body draftOrder:DraftOrderPost) : Response<DraftOrderPost>

    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @GET("admin/api/2023-04/draft_orders/{draft_order_id}.json")
    suspend fun getCartDraftOrder(@Path(value = "draft_order_id") draftId:Long) : Response<DraftOrderPost>

    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @PUT("admin/api/2023-04/draft_orders/{draftOrderId}.json")
    suspend fun updateCartDraftOrder(@Path(value = "draftOrderId") draftId:Long,@Body draftOrder:DraftOrderPost)
    @Headers("X-Shopify-Access-Token: ${Constants.accesstoken}")
    @POST("admin/api/2023-04/orders.json")
    suspend fun createOrder(@Body order:PostOrderModel) : Response<PostOrderModel>
}