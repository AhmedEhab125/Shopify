package com.example.shopify.orderDetails.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.Models.orderList.Order
import com.example.shopify.databinding.FragmentOrderListBinding
import com.example.shopify.home.viewModel.HomeViewModel
import com.example.shopify.nework.ApiState
import com.example.shopify.orderDetails.model.OrderListRepo
import com.example.shopify.orderDetails.viewModel.OrderListViewModel
import com.example.shopify.orderDetails.viewModel.OrderListViewModelFactory
import kotlinx.coroutines.launch


class OrderListFragment : Fragment() {
    lateinit var binding: FragmentOrderListBinding
    lateinit var orderListViewModel: OrderListViewModel
    lateinit var orderListViewModelFactory: OrderListViewModelFactory
    lateinit var myAdapter: OrderListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderListViewModelFactory =  OrderListViewModelFactory(OrderListRepo(
            com.example.shopify.repo.RemoteSource(com.example.shopify.nework.ShopifyAPi.retrofitService)
        ))
        orderListViewModel = ViewModelProvider(
            requireActivity(),
            orderListViewModelFactory
        ).get(OrderListViewModel::class.java)
        myAdapter = OrderListAdapter(listOf())
        binding.rvOrders.adapter = myAdapter
        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        setOrderList()
        orderListViewModel.getOrders(6930820727106)
    }
    fun setOrderList(){
        lifecycleScope.launch {
            orderListViewModel.accessOrderList.collect() { result ->
                when (result) {
                    is ApiState.Success<*> -> {
                       // homeBinding.progressBar.visibility = View.GONE

                        var orders = result.date as List<Order>

                       // smartCollections = brands?.smart_collections ?: listOf()
                      //  brandsAdapter.setBrandsList(smartCollections)
                        myAdapter.setOrderList(orders)
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