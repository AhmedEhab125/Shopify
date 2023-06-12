package com.example.shopify.signup.signUpViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.ProductDetalisInterface
import com.example.shopify.repo.RegisterUserInterFace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SignUpViewModel (var remoteSource: RegisterUserInterFace) : ViewModel() {
    private var _userInfo = MutableStateFlow<ApiState>(ApiState.Loading)
    var  productInfo : StateFlow<ApiState> = _userInfo
    fun registerUserToApi(user:CustomerRegistrationModel) {
        viewModelScope.launch(Dispatchers.IO) {
            remoteSource.createUserAtApi(user).catch { error ->
                _userInfo.value = ApiState.Failure(error)
            }.collect { myUser ->
                _userInfo.value = ApiState.Success(myUser)
                Log.i("USER",myUser?.customer?.email?:"Milad")
            }
        }

    }
}