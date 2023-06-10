package com.example.shopify.products.view

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.databinding.FragmentProductsBinding
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.products.model.CollectionProductsRepo
import com.example.shopify.products.viewModel.ProductsViewModel
import com.example.shopify.products.viewModel.ProductsViewModelFactory
import com.example.shopify.repo.RemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductsFragment : Fragment() {
    private lateinit var productsBinding: FragmentProductsBinding
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var viewModel: ProductsViewModel
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productsBinding = FragmentProductsBinding.inflate(inflater)
        productsAdapter = ProductsAdapter(listOf())
        val factory =
            ProductsViewModelFactory(CollectionProductsRepo(RemoteSource(ShopifyAPi.retrofitService)))
        viewModel = ViewModelProvider(requireActivity(), factory)[ProductsViewModel::class.java]
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("loading")
        return productsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataReceived = requireArguments().getLong("id")
        viewModel.getCollectionProducts(dataReceived)
        productsBinding.productsRV.adapter = productsAdapter
        productsBinding.productsRV.layoutManager = GridLayoutManager(requireContext(), 2)
        updateRecycleView()
    }

    private fun updateRecycleView() {
        lifecycleScope.launch {
            viewModel.collectionProducts.collect {
                when (it) {
                    is ApiState.Loading -> {
                        progressDialog.show()
                    }
                    is ApiState.Failure->{
                        progressDialog.hide()
                        print(it.error)
                    }
                    is ApiState.Success<*>->{
                        progressDialog.hide()
                        var collectionProduct = it.date as CollectProductsModel
                        productsAdapter.updateList(collectionProduct.products)
                    }
                }
            }
        }

    }


}