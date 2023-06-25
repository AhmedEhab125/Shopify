package com.example.shopify.Models.orderList

data class Customer(
    //val accepts_marketing: Boolean? =null ,
   // val accepts_marketing_updated_at: String?=null,
    // val admin_graphql_api_id: String?,
  //  val created_at: String?=null,
  //  val currency: String?,
    val default_address: DefaultAddress?,
    val email: String?,
  //  val email_marketing_consent: EmailMarketingConsent?,
    val first_name: String?,
    val id: Long?,
    val last_name: String?,
    val phone: String?,
 //   val sms_marketing_consent: SmsMarketingConsent?,
    val state: String?,
    val tags: String?,
  //  val tax_exempt: Boolean?,
  //  val updated_at: String?,
    val verified_email: Boolean?
)