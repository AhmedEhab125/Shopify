package com.example.shopify.payment.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.Models.postOrderModel.PostOrderModel
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IAddresses
import com.example.shopify.repo.IPostOrder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PaymentViewModel (var remoteSource: IPostOrder) : ViewModel() {
   private  var _order = MutableStateFlow<ApiState>(ApiState.Loading)
    var  accessOrder : StateFlow<ApiState> = _order
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    fun createOrder(order : PostOrderModel ) {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            remoteSource.createOrder(order).catch { error ->
                _order.value = ApiState.Failure(error)
            }.collect { addresses ->
                _order.value = ApiState.Success(addresses)

            }
        }


    }

}