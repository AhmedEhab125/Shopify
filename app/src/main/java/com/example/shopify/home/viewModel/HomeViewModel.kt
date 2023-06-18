package com.example.shopify.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IBrands
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(var remoteSource: IBrands) : ViewModel() {
    private var _brandsList = MutableStateFlow<ApiState>(ApiState.Loading)
    var  accessBrandsList : StateFlow<ApiState> = _brandsList

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    fun getBrands() {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            remoteSource.getBrands().catch { error ->
                _brandsList.value = ApiState.Failure(error)
            }.collect { brands ->
                _brandsList.value = ApiState.Success(brands)
            }
        }


    }

}