package com.example.shopify.repo

import android.util.Log
import com.example.shopify.Models.RetriveOreder.RetriveOrder
import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addAddress.Address
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.orderList.RetriveOrderModel
import com.example.shopify.Models.postOrderModel.PostOrderModel
import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.Models.registrashonModel.Addresse
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.nework.ShopifyApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RemoteSource() : RemoteSourceInterface {

    var network: ShopifyApiService = ShopifyAPi.retrofitService
    override suspend fun getBrands(): Flow<BrandModel?> {
        return flowOf(network.getBrands().body())
    }

    override suspend fun getProductDetalis(id: Long): Flow<ProductModel?> {
        return flowOf(network.getProductDetails(id).body())
    }

    override suspend fun getCollectionProducts(id: Long): Flow<CollectProductsModel?> {
        return flowOf(network.getCollectionProducts(id).body())
    }

    override suspend fun getAllProducts(): Flow<CollectProductsModel?> {
        return flowOf(network.getAllProducts().body())
    }

    override suspend fun createUserAtApi(user: CustomerRegistrationModel): Flow<CustomerRegistrationModel?> {
        return flowOf(network.registerUserAtApi(user).body())
    }

    override suspend fun createWishDraftOrder(draftOrder: DraftOrderPost): Flow<DraftOrderPost?> {
        return flowOf(network.createDraftOrder(draftOrder).body())
    }

    override suspend fun getAddresses(customerId: Long): Flow<AddressesModel?> {
        return flowOf(network.getCustomerAddresses(customerId).body())
    }

    override suspend fun removeAddresses(customerId: Long, addressId: Long) {
        network.removeCustomerAddresse(customerId,addressId)
    }

    override suspend fun addAddress(customerId: Long, addresse: AddNewAddress): Flow<AddressesModel?> {
        return flowOf(network.addCustomerAddresse(customerId, addresse).body())
    }

    override suspend fun getOrderList(): Flow<RetriveOrderModel?> {
       return  flowOf(network.getAllOrders().body())
    }

    override suspend fun getSelectedProducts(ids :String): Flow<CollectProductsModel?> {
        return flowOf(network.getSelectedProductsDetails(ids).body())
    }

    override suspend fun getCartItems(draftId:Long):Flow<DraftOrderPost?>  {
       return flowOf(network.getCartDraftOrder(draftId).body())
    }

    override suspend fun upDateCartOrderDraft(draftID:Long,draftOrder: DraftOrderPost) {
        var s =network.updateCartDraftOrder(draftID,draftOrder)
       if(s.isSuccessful) {
           Log.i("EssamFailureSuccess", ""+s.body())
       }else{
           Log.i("EssamFailure", ""+s.errorBody())
       }
    }

    override suspend fun createOrder(order: PostOrderModel): Flow<RetriveOrder?> {
        return  flowOf(network.createOrder(order).body())
    }
}