package com.example.shopify.addressPicker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addAddress.Address
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IAddCustomerAddress
import com.example.shopify.repo.IAddresses
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddAddressViewModel(var remoteSource: IAddCustomerAddress) : ViewModel() {
    private var _allAddressesList = MutableStateFlow<ApiState>(ApiState.Loading)
    var accessAllAddressesList: StateFlow<ApiState> = _allAddressesList
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    fun addAddresses(customerId: Long ,address: AddNewAddress) {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            remoteSource.addAddress(customerId,address).catch { error ->
                _allAddressesList.value = ApiState.Failure(error)
            }.collect { addresses ->
                _allAddressesList.value = ApiState.Success(addresses)
            }
        }


    }
}