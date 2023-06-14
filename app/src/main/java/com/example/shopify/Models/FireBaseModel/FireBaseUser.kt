package com.example.shopify.Models.FireBaseModel

data class MyFireBaseUser(
   var firsName :String? = null,
   var userId:Long? = null,
   var cartdraftOrderId : Long? = null,
   var whiDraftOedredId :Long? = null
) {
   constructor() : this(null, null, null,null)
}
