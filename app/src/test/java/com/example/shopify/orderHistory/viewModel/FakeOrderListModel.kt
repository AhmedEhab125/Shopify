package com.example.shopify.orderHistory.viewModel

import com.example.shopify.Models.orderList.*
import com.google.gson.Gson

fun fakeOrderList(): RetriveOrderModel? {
    var gson =Gson()
    var data = gson.fromJson(Data.data,RetriveOrderModel::class.java)
    var orders = listOf<Order>(Order(
        BillingAddress("2 st amjad","alex","Egypt"),
        "ahmed@gmail.com","2020/1/23","202.0"
        ,Customer(DefaultAddress(
            null,
            "16,Sidi Beshr Qebly",
            "Alexandria",
            "Egypt","",12231321,true,"asd",
            null,
            null,
            null,
            null,
            null
        ),"ahmed@gmail.com", "ahmed",12231321,"ehab","01011",null,null,null
        ),"ahmed@gmail.com", listOf(),null, ShippingAddress("2 st amjad","alex","Egypt"),""

    ))
    return data


}