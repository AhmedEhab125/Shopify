package com.example.shopify.category.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IAllProducts
import com.example.shopify.repo.IBrands
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CategoryViewModel (var remoteSource: IAllProducts) : ViewModel() {
    private var _allProductList = MutableStateFlow<ApiState>(ApiState.Loading)
    var  accessAllProductList : StateFlow<ApiState> = _allProductList
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            remoteSource.getAllProducts().catch { error ->
                _allProductList.value = ApiState.Failure(error)
            }.collect { brands ->
                _allProductList.value = ApiState.Success(brands)
            }
        }


    }

}