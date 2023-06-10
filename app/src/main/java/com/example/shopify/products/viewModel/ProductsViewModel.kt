package com.example.shopify.products.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.CollectionProductsInterface
import com.example.shopify.repo.IBrands
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductsViewModel(private var remoteSource: CollectionProductsInterface) : ViewModel() {
    private var _collectionProducts = MutableStateFlow<ApiState>(ApiState.Loading)
    var  collectionProducts : StateFlow<ApiState> = _collectionProducts
    fun getCollectionProducts(id:Long) {
        viewModelScope.launch(Dispatchers.IO) {
            remoteSource.getCollectionProducts(id).catch { error ->
                _collectionProducts.value = ApiState.Failure(error)
            }.collect { brands ->
                _collectionProducts.value = ApiState.Success(brands)
            }
        }
    }
}