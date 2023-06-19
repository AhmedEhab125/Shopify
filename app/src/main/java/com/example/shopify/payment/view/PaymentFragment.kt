package com.example.shopify.payment.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.shopify.Models.RetriveOreder.RetriveOrder
import com.example.shopify.Models.postOrderModel.Customer
import com.example.shopify.Models.postOrderModel.LineItem
import com.example.shopify.Models.postOrderModel.PostOrderModel
import com.example.shopify.R
import com.example.shopify.database.LocalDataSource
import com.example.shopify.databinding.FragmentPaymentBinding
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.payment.model.PaymentRepo
import com.example.shopify.payment.paymentNetwork.NetworkPayment
import com.example.shopify.payment.viewModel.PaymentViewModel
import com.example.shopify.payment.viewModel.PaymentViewModelFactory
import com.example.shopify.repo.RemoteSource
import com.example.shopify.utiltes.Constants
import com.example.shopify.utiltes.LoggedUserData
import com.google.android.material.snackbar.Snackbar
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class PaymentFragment : Fragment() {
    lateinit var binding: FragmentPaymentBinding
    lateinit var paymentViewModel: PaymentViewModel
    lateinit var paymentViewModelFactory: PaymentViewModelFactory
    lateinit var job: Job
    lateinit var itemList: MutableList<LineItem>
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var postOrder: PostOrderModel
    private lateinit var bundle:Bundle
    lateinit var curency:String
    private var discount = ""
    private var code = ""
    private var totalCost=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        PaymentConfiguration.init(requireContext(), Constants.publichKey)
        paymentSheet = PaymentSheet(this) {
            onPaymentSheetResult(it)
        }
        curency = when(Constants.currencyType ){
            "EGP" -> "EGP"
            else -> "usd"
        }
        binding = FragmentPaymentBinding.inflate(inflater)
        NetworkPayment.getCustomerId(requireContext(),(calcTotalPrice() + 200),curency)
        binding.paymentProgressBar.visibility = View.GONE
        binding.trueLottie.visibility = View.GONE
        binding.tvDiscount.text = "0"
        binding.tvDelevaryFees.text = "200 ${Constants.currencyType}"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymentViewModelFactory = PaymentViewModelFactory(
            PaymentRepo(
                RemoteSource(ShopifyAPi.retrofitService)
            )
        )
        job = Job()
        paymentViewModel = ViewModelProvider(
            requireActivity(),
            paymentViewModelFactory
        )[PaymentViewModel::class.java]
        itemList = mutableListOf()
        LoggedUserData.orderItemsList.forEach { item ->
            val productId = item.sku?.split(",")?.first()?.toLong()
            val variantId = item.sku?.split(",")?.get(1)?.toLong()

            println(item.sku)
            var data = productId?.let { pro_id->
                if (variantId != null) {
                    item.quantity?.let { variant_id ->

                        itemList.add( LineItem(pro_id, variant_id, variantId) )
                    }
                }
            }

        }
        binding.btnCheckout.setOnClickListener {
            if (binding.cashOnDelevaryRB.isChecked) {
                postOrder = PostOrderModel(
                    com.example.shopify.Models.postOrderModel.Order("Cash",
                        "EGP", "150",
                        Customer(
                            LocalDataSource.getInstance().readFromShared(requireContext())?.firsName ?: "no name",
                            LocalDataSource.getInstance().readFromShared(requireContext())?.userId ?: 1L,
                            "")
                        , itemList, Constants.selectedAddress!!, discount
                    )
                )
                createOrder(postOrder)
                val index = Constants.vouchersList.indexOf(code)
                if(index>=0)
                    Constants.vouchersList[index] = "null"
            } else if (binding.onlinePaymentRB.isChecked) {
                Log.i("essam order online", "$discount")
                postOrder = PostOrderModel(
                    com.example.shopify.Models.postOrderModel.Order(
                        "Credit", "EGP", "150",
                        Customer(LocalDataSource.getInstance().readFromShared(requireContext())?.firsName ?: "no name",
                            LocalDataSource.getInstance().readFromShared(requireContext())?.userId ?: 1L, "")
                        , itemList, Constants.selectedAddress!!, discount
                    )
                )
                paymentFlow()
            } else {
                Snackbar.make(
                    binding.btnCheckout,
                    "Please Choose the method of payment.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            Log.i("essam checkout btn", "$discount")
            discount = ""
        }
        calcTotalPrice()
        binding.tvTotalFees.text = "${calcTotalPrice() + 200} ${Constants.currencyType}"
        voucherListen()
    }

    private fun calcTotalPrice():Int {
        var count = 0F
        LoggedUserData.orderItemsList.forEach { item ->
            count += (item.price!!.toFloat()) * (item.quantity!!) * Constants.currencyValue
        }

        binding.tvProductFees.text = "${(count).toInt()} ${ Constants.currencyType}"
        return (count).toInt()
    }

    private fun voucherListen(){
        binding.voutcherTF.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                check(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun check(text:String) {
        if( text == Constants.vouchersList[0] ||
            text == Constants.vouchersList[1] ||
            text == Constants.vouchersList[2] ||
            text == Constants.vouchersList[3] ||
            text == Constants.vouchersList[4] ||
            text == Constants.vouchersList[5]
        ){
            NetworkPayment.getCustomerId(requireContext(),(calcTotalPrice() - (calcTotalPrice()*.20).toInt() + 200),curency)
            discount = if(Constants.currencyType == "EGP"){
                "${(calcTotalPrice()*.20)/Constants.currencyValue}"
                 }else{
                     "${(calcTotalPrice()*.20)}"
                 }
            Log.i("essam order check", "$discount")
            binding.trueLottie.visibility = View.VISIBLE
            binding.tvDiscount.text = "${(calcTotalPrice()*.20).toInt()}"
            totalCost = calcTotalPrice() - (calcTotalPrice()*.20).toInt() + 200
            binding.tvTotalFees.text = "${totalCost} ${ Constants.currencyType}"
            code = text
        }else {
            NetworkPayment.getCustomerId(requireContext(),(calcTotalPrice() + 200),curency)
            discount = "0"
            binding.trueLottie.visibility = View.GONE
            binding.tvDiscount.text = "0"
            totalCost = calcTotalPrice() + 200
            binding.tvTotalFees.text = "${totalCost} ${Constants.currencyType}"
        }
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    private fun createOrder(order: PostOrderModel) {
        paymentViewModel.createOrder(order)
        observeOrderCreated()
    }

    fun observeOrderCreated() {
        job = lifecycleScope.launch(Dispatchers.IO) {
            paymentViewModel._order.collectLatest { result ->
                when (result) {
                    is ApiState.Success<*> -> {

                        var order = result.date as RetriveOrder?
                        withContext(Dispatchers.Main) {
                            //binding.paymentProgressBar.visibility = View.VISIBLE

                            if (order != null) {
                                bundle = Bundle().apply {
                                    putSerializable("order", order.order)
                                }
                                Navigation.findNavController(requireView()).navigate(
                                    R.id.action_paymentFragment_to_orderDetailsFragment,
                                    bundle
                                )
                                Toast.makeText( requireContext(),"order set succssfully",Toast.LENGTH_LONG ).show()
                                Toast.makeText(requireContext(), "Payment Success!!", Toast.LENGTH_SHORT).show()
                                paymentViewModel._order.value=ApiState.Loading
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "order not set ",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), "order not set ", Toast.LENGTH_LONG).show()
                    }
                    is ApiState.Loading -> {

                    }
                }
            }
        }
    }

    private fun paymentFlow() {
        lifecycleScope.launch(Dispatchers.Main){
            binding.paymentProgressBar.visibility = View.VISIBLE
            delay(3000)
            binding.paymentProgressBar.visibility = View.GONE
            paymentSheet.presentWithPaymentIntent(
                NetworkPayment.clientSecret, PaymentSheet.Configuration(
                    "Shopify",
                    PaymentSheet.CustomerConfiguration(
                        NetworkPayment.customerId,
                        NetworkPayment.sphericalKey
                    )
                )
            )
        }
    }
    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {

        if (paymentSheetResult is PaymentSheetResult.Completed) {
            val index = Constants.vouchersList.indexOf(code)
            if(index>=0)
                Constants.vouchersList[index] = "null"
            createOrder(postOrder)
        }else{
            Toast.makeText(requireContext(), "Payment Canceled", Toast.LENGTH_SHORT).show()
        }
    }

}