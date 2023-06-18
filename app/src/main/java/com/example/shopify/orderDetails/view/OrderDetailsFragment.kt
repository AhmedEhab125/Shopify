package com.example.shopify.orderDetails.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.Models.orderList.Order
import com.example.shopify.Models.productDetails.Product
import com.example.shopify.databinding.FragmentOrderDetailsBinding
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
            binding.tvOrderPrice.text = "${order.current_total_price?.toDouble()
                ?.times(Constants.currencyValue)}  ${Constants.currencyType}"
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

}