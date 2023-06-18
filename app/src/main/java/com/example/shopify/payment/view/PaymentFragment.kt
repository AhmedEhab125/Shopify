package com.example.shopify.payment.view

import android.os.Bundle
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        PaymentConfiguration.init(requireContext(), Constants.publichKey)
        paymentSheet = PaymentSheet(this) {
            onPaymentSheetResult(it)
        }
        NetworkPayment.getCustomerId(requireContext())
        binding = FragmentPaymentBinding.inflate(inflater)
        binding.paymentProgressBar.visibility = View.GONE
        // Inflate the layout for this fragment
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
        ).get(PaymentViewModel::class.java)
        itemList = mutableListOf()




        LoggedUserData.orderItemsList.forEach { item ->
            var productId = item.sku?.split(",")?.first()?.toLong()
            var variantId = item.sku?.split(",")?.get(1)?.toLong()
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

            postOrder = PostOrderModel(
                com.example.shopify.Models.postOrderModel.Order(
                    "Cash",
                    "EGP",
                    "150",
                    Customer(
                        LocalDataSource.getInstance().readFromShared(requireContext())?.firsName
                            ?: "no name",
                        LocalDataSource.getInstance().readFromShared(requireContext())?.userId
                            ?: 1L,
                        ""
                    ), itemList, Constants.selectedAddress!!, "0"
                )
            )
            if (binding.cashOnDelevaryRB.isChecked) {
                createOrder(postOrder)
            } else if (binding.onlinePaymentRB.isChecked) {
                paymentFlow()
            } else {
                Snackbar.make(
                    binding.btnCheckout,
                    "Please Choose the method of payment.",
                    Snackbar.LENGTH_LONG
                ).show()
            }


        }

    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    fun createOrder(order: PostOrderModel) {
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
                                // binding.paymentProgressBar.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    "order set succssfully",
                                    Toast.LENGTH_LONG
                                ).show()
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
        //createOrder(postOrder)
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
            Toast.makeText(requireContext(), "Payment Success!!", Toast.LENGTH_SHORT).show()
            createOrder(postOrder)
        }else{
            Toast.makeText(requireContext(), "Payment Canceled", Toast.LENGTH_SHORT).show()
        }
    }
}