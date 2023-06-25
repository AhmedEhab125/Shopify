package com.example.shopify.signup.signUpViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.ProductDetalisInterface
import com.example.shopify.repo.RegisterUserInterFace
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SignUpViewModel (var remoteSource: RegisterUserInterFace) : ViewModel() {
    private var _userInfo = MutableStateFlow<ApiState>(ApiState.Loading)
    var  userInfo : StateFlow<ApiState> = _userInfo

    private var _wishListDraftOrder = MutableStateFlow<ApiState>(ApiState.Loading)
    var wishListDraftOrder = _wishListDraftOrder

    private var _draftListDraftOrder = MutableStateFlow<ApiState>(ApiState.Loading)
    var cartListDraftOrder = _draftListDraftOrder
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    fun registerUserToApi(user:CustomerRegistrationModel) {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            remoteSource.createUserAtApi(user).catch { error ->
                _userInfo.value = ApiState.Failure(error)
            }.collect { myUser ->
                _userInfo.value = ApiState.Success(myUser)
            }
        }
    }



    fun postWishListDraftPrder(draftOrder: DraftOrderPost){
        viewModelScope.launch (Dispatchers.IO+coroutineExceptionHandler) {
            remoteSource.createWishDraftOrder(draftOrder).catch { error ->
                _wishListDraftOrder.value = ApiState.Failure(error)
            }.collect{ draft ->
                _wishListDraftOrder.value = ApiState.Success(draft)
            }
        }
    }



    fun postCartDraftOrder(draftOrder: DraftOrderPost){
        viewModelScope.launch (Dispatchers.IO+coroutineExceptionHandler){
            remoteSource.createWishDraftOrder(draftOrder).catch { error ->
                _draftListDraftOrder.value = ApiState.Failure(error)
            }.collect{ draft ->
                _draftListDraftOrder.value = ApiState.Success(draft)
            }
        }
    }

}