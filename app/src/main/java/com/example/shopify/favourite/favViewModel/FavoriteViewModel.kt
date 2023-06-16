package com.example.shopify.favourite.favViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.ICart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class FavoriteViewModel ( var remoteSource: ICart) : ViewModel() {
    private var _favItems = MutableStateFlow<ApiState>(ApiState.Loading)
    var  favItems : StateFlow<ApiState> = _favItems

    fun getFavItems(draftId:Long) {
        viewModelScope.launch(Dispatchers.IO) {
            remoteSource.getCartItems(draftId).catch { error ->
                _favItems.value = ApiState.Failure(error)
            }.collect { brands ->
                _favItems.value = ApiState.Success(brands)
            }
        }
    }
    fun updateFavtItem(draftId:Long,draftOrderPost: DraftOrderPost){
        viewModelScope.launch(Dispatchers.IO) {
            remoteSource.upDateCartOrderDraft(draftId,draftOrderPost)
        }
    }
}
