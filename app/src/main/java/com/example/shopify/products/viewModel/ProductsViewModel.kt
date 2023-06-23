package com.example.shopify.products.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.nework.ApiState
import com.example.shopify.products.model.ICollectionProductsRepo
import com.example.shopify.repo.CollectionProductsInterface
import com.example.shopify.repo.IBrands
import com.example.shopify.repo.RemoteSourceInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductsViewModel(private var remoteSource: ICollectionProductsRepo) : ViewModel() {
    private var _collectionProducts = MutableStateFlow<ApiState>(ApiState.Loading)
    var  collectionProducts : StateFlow<ApiState> = _collectionProducts
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    private var _productList = MutableStateFlow<ApiState>(ApiState.Loading)
    var  accessProductList : StateFlow<ApiState> = _productList
    fun getCollectionProducts(id:Long) {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            remoteSource.getCollectionProducts(id).catch { error ->
                _collectionProducts.value = ApiState.Failure(error)
            }.collect { brands ->
                _collectionProducts.value = ApiState.Success(brands)

            }
        }
    }
    fun getSelectedProducts(ids : String) {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            remoteSource.getSelectedProducts(ids).catch { error ->
                _productList.value = ApiState.Failure(error)
            }.collect { products ->
                _productList.value = ApiState.Success(products?.products ?: listOf())
            }
        }


    }
}