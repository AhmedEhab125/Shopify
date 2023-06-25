package com.example.shopify.utiltes

import com.example.shopify.Models.draftOrderCreation.LineItem

object LoggedUserData {
    var orderItemsList:MutableList<LineItem> = mutableListOf()
    var favOrderDraft:MutableList<LineItem> = mutableListOf()
}