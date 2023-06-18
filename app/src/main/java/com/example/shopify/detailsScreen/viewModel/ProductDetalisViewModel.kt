package com.example.shopify.detailsScreen.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IBrands
import com.example.shopify.repo.ProductDetalisInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductDetalisViewModel (var remoteSource: ProductDetalisInterface) : ViewModel() {
    private var _productInfo = MutableStateFlow<ApiState>(ApiState.Loading)
    var  productInfo : StateFlow<ApiState> = _productInfo
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    fun getProductDetalis(id:Long) {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            remoteSource.getProductDetalis(id).catch { error ->
                _productInfo.value = ApiState.Failure(error)
            }.collect { myProduct ->
                _productInfo.value = ApiState.Success(myProduct)
            }
        }

    }

}