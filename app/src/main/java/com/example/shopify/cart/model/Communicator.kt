package com.example.shopify.cart.model

interface Communicator {
    fun addItem(position:Int,amount:Int)
    fun subItem(position:Int,amount:Int)
}