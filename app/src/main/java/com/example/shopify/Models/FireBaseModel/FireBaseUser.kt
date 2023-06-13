package com.example.shopify.Models.FireBaseModel

data class MyFireBaseUser(
   var firsName :String? = null,
   var userId:Long? = null,
   var draftOrderId : Long? = null
) {
   constructor() : this(null, null, null)
}
