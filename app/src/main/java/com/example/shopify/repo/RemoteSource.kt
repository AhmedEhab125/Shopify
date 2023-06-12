package com.example.shopify.repo

import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addAddress.Address
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.Models.registrashonModel.Addresse
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.nework.ShopifyApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RemoteSource(var network: ShopifyApiService) : IBrands, ProductDetalisInterface,
    CollectionProductsInterface, IAllProducts, RegisterUserInterFace, IAddresses,
    IAddCustomerAddress {
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

    override suspend fun getAddresses(customerId: Long): Flow<AddressesModel?> {
        return flowOf(network.getCustomerAddresses(customerId).body())
    }

    override suspend fun addAddress(customerId: Long, addresse: AddNewAddress): Flow<AddressesModel?> {
        return flowOf(network.addCustomerAddresse(customerId, addresse).body())
    }
}