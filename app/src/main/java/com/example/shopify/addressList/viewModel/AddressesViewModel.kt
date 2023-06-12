package com.example.shopify.addressList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IAddresses
import com.example.shopify.repo.IAllProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddressesViewModel (var remoteSource: IAddresses) : ViewModel() {
    private var _allAddressesList = MutableStateFlow<ApiState>(ApiState.Loading)
    var  accessAllAddressesList : StateFlow<ApiState> = _allAddressesList
    fun getAllAddresses(customerId : Long = 6955526881602) {
        viewModelScope.launch(Dispatchers.IO) {
            remoteSource.getAddresses(customerId).catch { error ->
                _allAddressesList.value = ApiState.Failure(error)
            }.collect { addresses ->
                _allAddressesList.value = ApiState.Success(addresses)
            }
        }


    }

}