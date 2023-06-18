package com.example.shopify.orderDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IOrderList
import com.example.shopify.repo.ISelectedProducts
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class OrderDetailsViewModel (var remoteSource: ISelectedProducts) : ViewModel() {
    private var _productList = MutableStateFlow<ApiState>(ApiState.Loading)
    var  accessProductList : StateFlow<ApiState> = _productList
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
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