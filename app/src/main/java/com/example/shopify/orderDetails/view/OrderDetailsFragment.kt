package com.example.shopify.orderDetails.view

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.constraintlayout.widget.Constraints
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.Models.orderList.Order
import com.example.shopify.Models.productDetails.Product
import com.example.shopify.R
import com.example.shopify.databinding.FragmentOrderDetailsBinding
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.orderDetails.model.OrderDetailsRepo
import com.example.shopify.orderDetails.viewModel.OrderDetailsViewModel
import com.example.shopify.orderDetails.viewModel.OrderDetailsViewModelFactory
import com.example.shopify.orderHistory.model.OrderListRepo
import com.example.shopify.orderHistory.viewModel.OrderListViewModel
import com.example.shopify.orderHistory.viewModel.OrderListViewModelFactory
import com.example.shopify.repo.RemoteSource
import com.example.shopify.utiltes.Constants
import kotlinx.coroutines.launch

class OrderDetailsFragment : Fragment() {
    lateinit var binding: FragmentOrderDetailsBinding
    lateinit var myAdapter: OrderDetailsAdapter
    lateinit var orderDetailsViewModelFactory: OrderDetailsViewModelFactory
    lateinit var orderDetailsViewModel: OrderDetailsViewModel
    lateinit var order : Order


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetailsBinding.inflate(inflater)
        when(arguments?.getString("from")){
            "payment" -> {
                doneDialog()
                binding.BkHome.setOnClickListener {
                    activity?.recreate()
                    Navigation.findNavController(requireView()).navigate(R.id.action_orderDetailsFragment_to_homeFragment)
                }

            }
            else->{
                binding.BkHome.visibility = View.GONE
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderDetailsViewModelFactory = OrderDetailsViewModelFactory(
            OrderDetailsRepo(
                RemoteSource(ShopifyAPi.retrofitService)
            )
        )
        orderDetailsViewModel = ViewModelProvider(
            requireActivity(),
            orderDetailsViewModelFactory
        ).get(OrderDetailsViewModel::class.java)
        myAdapter = OrderDetailsAdapter(listOf(), listOf())
        order = (arguments?.getSerializable("order") as? Order)!!
        setProductList()
        var ids =""
        order.line_items?.forEach { item -> ids+="${item.product_id}," }
        println(ids)
        orderDetailsViewModel.getSelectedProducts(ids)
    }

    fun setProductList() {
        binding.rvOrderProducts.adapter = myAdapter
        binding.rvOrderProducts.layoutManager = LinearLayoutManager(requireContext())
        if (order != null) {
            val address =
                "${order.shipping_address?.address1} ${order.shipping_address?.city} ${order.shipping_address?.country}"
            binding.tvOrderEmail.text = "${order.customer?.first_name} ${order.customer?.last_name}"
            binding.tvOrderAddress.text = address
            binding.tvOrderPhome.text = order.customer?.phone ?: ""
            binding.tvOrderPrice.text = "${
                (order.current_total_price?.toDouble()
                    ?.times(Constants.currencyValue))?.toInt()?.plus(50)
            }  ${Constants.currencyType}"
            binding.tvPaymentMethod.text = order.tags

            lifecycleScope.launch {
                orderDetailsViewModel.accessProductList.collect() { result ->
                    when (result) {
                        is ApiState.Success<*> -> {
                            // homeBinding.progressBar.visibility = View.GONE

                            var apiProduct = result.date as List<Product>
                            order?.line_items?.let { myAdapter.setProductList(it, apiProduct) }

                        }
                        is ApiState.Failure -> {
                            //  homeBinding.progressBar.visibility = View.GONE

                        }
                        is ApiState.Loading -> {
                            //   homeBinding.progressBar.visibility = View.VISIBLE
                        }

                    }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(false)
    }
    private fun doneDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.congratulation_dialog)
        val window: Window? = dialog.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }
}