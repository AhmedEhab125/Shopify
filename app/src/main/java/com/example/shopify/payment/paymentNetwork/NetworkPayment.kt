package com.example.shopify.payment.paymentNetwork

import android.content.Context
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.shopify.nework.ApiState
import com.example.shopify.payment.model.ThirdPartyResponse
import com.example.shopify.utiltes.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONException
import org.json.JSONObject

object NetworkPayment {
    lateinit var customerId: String
    lateinit var sphericalKey: String
    lateinit var clientSecret: String
    private var _paymentList = MutableStateFlow(ThirdPartyResponse("","",""))
    var  paymentList : StateFlow<ThirdPartyResponse> = _paymentList
    fun getCustomerId(context: Context,totalCost:Int,currency:String) {
        val request: StringRequest = object : StringRequest(
            Method.POST, "https://api.stripe.com/v1/customers",
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    customerId = jsonObject.getString("id")
                    //Toast.makeText(context, "Customer Id: $customerId", Toast.LENGTH_SHORT).show()
                    getSphericalKey(customerId, context,totalCost,currency)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                Toast.makeText(context, "Error in Listener", Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val header: HashMap<String, String> = HashMap<String, String>()
                header["Authorization"] = "Bearer ${Constants.paymentSecretKey}"
                return header
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }

    private fun getSphericalKey(customerId: String, context: Context,totalCost:Int,currency:String) {
        val request: StringRequest = object : StringRequest(
            Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    sphericalKey = jsonObject.getString("id")
                    // Toast.makeText(context, "Epherical Key: $sphericalKey",Toast.LENGTH_SHORT).show()
                    getClientSecret(customerId, context,totalCost,currency)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                //
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val header: HashMap<String, String> = HashMap<String, String>()
                header["Authorization"] = "Bearer ${Constants.paymentSecretKey}"
                header["Stripe-Version"] = "2020-08-27"
                return header
            }

            override fun getParams(): Map<String, String> {
                val param: HashMap<String, String> = HashMap<String, String>()
                param["customer"] = customerId
                return param
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }

    private fun getClientSecret(customerId: String, context: Context,totalCost:Int,currency:String) {
        val request: StringRequest =
            object : StringRequest(Method.POST, "https://api.stripe.com/v1/payment_intents",
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        clientSecret = jsonObject.getString("client_secret")
                        _paymentList.value= ThirdPartyResponse(customerId, sphericalKey,
                            clientSecret)
                        //  Toast.makeText(context, "Client Secret: $clientSecret",Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    //
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val header: HashMap<String, String> = HashMap<String, String>()
                    header["Authorization"] = "Bearer ${Constants.paymentSecretKey}"
                    return header
                }

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val param: HashMap<String, String> = HashMap<String, String>()
                    param["customer"] = customerId
                    param["amount"] = "${totalCost*100}"
                    param["currency"] = currency
                    param["automatic_payment_methods[enabled]"] = "true"
                    return param
                }
            }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }


}