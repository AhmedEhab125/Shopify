package com.example.shopify.cart.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IBrands
import com.example.shopify.repo.ICart
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch

class CartViewModel(var remoteSource: ICart) : ViewModel() {
    private var _cartItems = MutableStateFlow<ApiState>(ApiState.Loading)
    var  accessCartItems : StateFlow<ApiState> = _cartItems
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    fun getCartItems(draftId:Long) {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            remoteSource.getCartItems(draftId).catch { error ->
                _cartItems.value = ApiState.Failure(error)
            }.collect { brands ->
                _cartItems.value = ApiState.Success(brands)
            }
        }
    }
    fun updateCartItem(draftId:Long,draftOrderPost: DraftOrderPost){
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            remoteSource.upDateCartOrderDraft(draftId,draftOrderPost)
        }
    }

}