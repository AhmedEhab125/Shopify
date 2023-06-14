package com.example.shopify.Models.orderList

import java.io.Serializable

data class Order(
    val billing_address: BillingAddress?,
//    val buyer_accepts_marketing: Boolean?,

    //  val confirmed: Boolean?,
    //  val contact_email: String?,
    val created_at: String?,
    // val currency: String?,
    /*
     val current_subtotal_price: String?,
      val current_subtotal_price_set: CurrentSubtotalPriceSet?,
      val current_total_discounts: String?,
      val current_total_discounts_set: CurrentTotalDiscountsSet?,
      */
    val current_total_price: String?,
    /*   val current_total_price_set: CurrentTotalPriceSet?,
       val current_total_tax: String?,
       val current_total_tax_set: CurrentTotalTaxSet?,
       */
    val customer: Customer?,

    val email: String?,
    /* val estimated_taxes: Boolean?,
     val financial_status: String?,

     val id: Long?,*/
    val line_items: List<LineItem>?,

    // val name: String?,
    //   val number: Int?,
    // val order_number: Int?,
    /* val order_status_url: String?,
     val payment_gateway_names: List<String>?,

     val presentment_currency: String?,
     val processed_at: String?,
     */
    val shipping_address: ShippingAddress?,
    /*
    val source_name: String?,
    val subtotal_price: String?,
    val subtotal_price_set: SubtotalPriceSet?,
    val tags: String?,
    val tax_lines: List<TaxLine>?,
    val taxes_included: Boolean?,
    val test: Boolean?,
    val token: String?,
    val total_discounts: String?,
    val total_discounts_set: TotalDiscountsSet?,
    val total_line_items_price: String?,
    val total_line_items_price_set: TotalLineItemsPriceSet?,
    val total_outstanding: String?,
    val total_price: String?,
    val total_price_set: TotalPriceSet?,
    val total_shipping_price_set: TotalShippingPriceSet?,
    val total_tax: String?,
    val total_tax_set: TotalTaxSet?,
    val total_tip_received: String?,
    val total_weight: Int?,
    val updated_at: String?,
    */
) : Serializable