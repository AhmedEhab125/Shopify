package com.example.shopify.orderDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IOrderList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class OrderListViewModel (var remoteSource: IOrderList) : ViewModel() {
    private var _orderList = MutableStateFlow<ApiState>(ApiState.Loading)
    var  accessOrderList : StateFlow<ApiState> = _orderList
    fun getOrders(customerId : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            remoteSource.getOrderList().catch { error ->
                _orderList.value = ApiState.Failure(error)
            }.collect { orders ->
                var data = orders?.orders?.filter { order->  order.customer?.id!=null && order.customer.id == customerId }
                _orderList.value = ApiState.Success(data)
            }
        }


    }

}