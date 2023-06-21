package com.example.shopify.products.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopify.Models.productDetails.Product
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.databinding.FragmentProductsBinding
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.products.model.CollectionProductsRepo
import com.example.shopify.products.viewModel.ProductsViewModel
import com.example.shopify.products.viewModel.ProductsViewModelFactory
import com.example.shopify.repo.RemoteSource
import kotlinx.coroutines.launch


class ProductsFragment : Fragment(),OnClickToShowDetails {
    private lateinit var productsBinding: FragmentProductsBinding
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var viewModel: ProductsViewModel
    lateinit var myProducts : List<Product>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productsBinding = FragmentProductsBinding.inflate(inflater)
        productsAdapter = ProductsAdapter(listOf(),this)
        val factory =
            ProductsViewModelFactory(CollectionProductsRepo(RemoteSource(ShopifyAPi.retrofitService)))
        viewModel = ViewModelProvider(requireActivity(), factory)[ProductsViewModel::class.java]
        return productsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataReceived = requireArguments().getLong("id")
        viewModel.getCollectionProducts(dataReceived)
        productsBinding.productsRV.adapter = productsAdapter
        productsBinding.productsRV.layoutManager = GridLayoutManager(requireContext(), 2)
        updateRecycleView()
        searchForProduct()

    }

    private fun updateRecycleView() {
        lifecycleScope.launch {
            viewModel.collectionProducts.collect {
                when (it) {
                    is ApiState.Loading -> {
                       productsBinding.progressBar2.visibility = View.VISIBLE
                    }
                    is ApiState.Failure->{
                        productsBinding.progressBar2.visibility = View.GONE
                        print(it.error)
                    }
                    is ApiState.Success<*>->{
                        productsBinding.progressBar2.visibility = View.GONE
                        var collectionProduct = it.date as CollectProductsModel
                        myProducts = collectionProduct.products
                        productsAdapter.updateList(myProducts)
                    }
                }
            }
        }

    }

    fun searchForProduct(){
        productsBinding.productSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    fun filterProducts(text:String){
       var filtteredProducts = mutableListOf<Product>()
        for (product in myProducts){
            if (product.title?.lowercase()?.contains(text.lowercase())!!){
                filtteredProducts.add(product)
            }
        }
        productsAdapter.updateList(filtteredProducts)
        if (filtteredProducts.isEmpty()){
            Toast.makeText(requireContext(),"Sorry,No Data Founded", Toast.LENGTH_SHORT).show()
        }

    }

    override fun ShowProductDetalis(productId: Long) {
        val action = ProductsFragmentDirections.fromProductToDetails(productId)
        Navigation.findNavController(requireView()).navigate(action)
    }


}