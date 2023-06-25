package com.example.shopify.home.viewModel

import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.Models.brands.Image
import com.example.shopify.Models.brands.SmartCollection

fun getFakeBrands(): BrandModel? {
        return BrandModel(listOf())
    }

fun getFakeResponseOfSmartCollections() : BrandModel {
    var samrtCollections = listOf(SmartCollection("","",false,"",1325464654, Image("","",1,"",20),"","",
        listOf(),"","","",""
    ))

    return BrandModel(samrtCollections)
}