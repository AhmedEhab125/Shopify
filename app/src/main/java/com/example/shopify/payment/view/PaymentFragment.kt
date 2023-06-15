package com.example.shopify.payment.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.shopify.Models.RetriveOreder.RetriveOrder
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.Models.postOrderModel.Customer
import com.example.shopify.Models.postOrderModel.LineItem
import com.example.shopify.Models.postOrderModel.PostOrderModel
import com.example.shopify.Models.postOrderModel.ShippingAddress
import com.example.shopify.R
import com.example.shopify.database.LocalDataSource
import com.example.shopify.databinding.FragmentPaymentBinding
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.orderDetails.model.OrderDetailsRepo
import com.example.shopify.orderDetails.viewModel.OrderDetailsViewModel
import com.example.shopify.orderDetails.viewModel.OrderDetailsViewModelFactory
import com.example.shopify.payment.model.PaymentRepo
import com.example.shopify.payment.viewModel.PaymentViewModel
import com.example.shopify.payment.viewModel.PaymentViewModelFactory
import com.example.shopify.repo.RemoteSource
import kotlinx.coroutines.*

class PaymentFragment : Fragment() {
lateinit var binding: FragmentPaymentBinding
lateinit var paymentViewModel: PaymentViewModel
lateinit var paymentViewModelFactory: PaymentViewModelFactory
lateinit var job :Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentPaymentBinding.inflate(inflater)
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
        paymentViewModel = ViewModelProvider(
            requireActivity(),
            paymentViewModelFactory
        ).get(PaymentViewModel::class.java)

        binding.btnCheckout.setOnClickListener {
            observeOrderCreated()
            var order = PostOrderModel(
                com.example.shopify.Models.postOrderModel.Order("Cash",
                    //  BillingAddress("2st amjad ", "alex", "Egypt"),
                    "EGP",
                    "150",
                    Customer(
                        "ahmed",
                        LocalDataSource.getInstance().readFromShared(requireContext())?.userId
                            ?: 1L,
                        ""
                    ), listOf(
                        LineItem("",8350701584706,5,"",45237616247106),
                        LineItem("",8350702338370,6,"",45237617230146)
                    )
                    , ShippingAddress("2st amjad ", "alex", "Egypt","ahmed","01021401193","ehab"),"5"
                )
            )
            createOrder(order)
        }

    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }
    fun createOrder(order : PostOrderModel){
        paymentViewModel.createOrder(order)
    }
    fun observeOrderCreated(){
     job =  lifecycleScope.launch(Dispatchers.IO) {
            paymentViewModel.accessOrder.collect{
                    result ->
                when (result) {
                    is ApiState.Success<*> -> {

                        var order = result.date as RetriveOrder?
                        withContext(Dispatchers.Main){


                         if (order!=null){
                             val bundle = Bundle().apply {
                                 putSerializable("order", order.order)
                             }
                             Navigation.findNavController(requireView()).navigate(R.id.action_paymentFragment_to_orderDetailsFragment,bundle)

                             Toast.makeText(requireContext(),"order set succssfully",Toast.LENGTH_LONG).show()
                         }else{
                             Toast.makeText(requireContext(),"order not set ",Toast.LENGTH_LONG).show()

                         }
                    }
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(),"order not set ",Toast.LENGTH_LONG).show()

                    }
                    is ApiState.Loading -> {

                    }

                }

            }

        }


    }


}