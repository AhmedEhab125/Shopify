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
import com.example.shopify.payment.viewModel.PaymentViewModel
import com.example.shopify.payment.viewModel.PaymentViewModelFactory
import com.example.shopify.repo.RemoteSource
import com.example.shopify.utiltes.Constants
import com.example.shopify.utiltes.LoggedUserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentFragment : Fragment() {
    lateinit var binding: FragmentPaymentBinding
    lateinit var paymentViewModel: PaymentViewModel
    lateinit var paymentViewModelFactory: PaymentViewModelFactory
    lateinit var job: Job
    lateinit var itemList: MutableList<LineItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater)
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
            observeOrderCreated()
            var order = PostOrderModel(
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
                    ),

                       itemList
                    ,
                    // ShippingAddress("2st amjad ", "alex", "Egassypt", "ahmed", "012", "ehab"),
                    Constants.selectedAddress!!,
                    "0"
                )
            )
            createOrder(order)
        }

    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    fun createOrder(order: PostOrderModel) {
        paymentViewModel.createOrder(order)
    }

    fun observeOrderCreated() {
        job = lifecycleScope.launch(Dispatchers.IO) {
            paymentViewModel.accessOrder.collect { result ->
                when (result) {
                    is ApiState.Success<*> -> {

                        var order = result.date as RetriveOrder?
                        withContext(Dispatchers.Main) {


                            if (order != null) {
                                val bundle = Bundle().apply {
                                    putSerializable("order", order.order)
                                }
                                Navigation.findNavController(requireView()).navigate(
                                    R.id.action_paymentFragment_to_orderDetailsFragment,
                                    bundle
                                )

                                Toast.makeText(
                                    requireContext(),
                                    "order set succssfully",
                                    Toast.LENGTH_LONG
                                ).show()
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


}