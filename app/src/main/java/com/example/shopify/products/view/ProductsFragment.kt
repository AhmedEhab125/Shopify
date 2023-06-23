package com.example.shopify.products.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopify.Models.productDetails.Product
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.R
import com.example.shopify.ckeckNetwork.InternetStatus
import com.example.shopify.ckeckNetwork.NetworkConectivityObserver
import com.example.shopify.ckeckNetwork.NetworkObservation
import com.example.shopify.databinding.FragmentProductsBinding
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.nework.ApiState
import com.example.shopify.products.model.CollectionProductsRepo
import com.example.shopify.products.viewModel.ProductsViewModel
import com.example.shopify.products.viewModel.ProductsViewModelFactory
import com.example.shopify.repo.RemoteSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ProductsFragment : Fragment(), OnClickToShowDetails {
    private lateinit var productsBinding: FragmentProductsBinding
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var viewModel: ProductsViewModel
    lateinit var myProducts: List<Product>
    lateinit var networkObservation: NetworkObservation
    var ids = ""
    lateinit var filterList : List<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productsBinding = FragmentProductsBinding.inflate(inflater)
        productsAdapter = ProductsAdapter(listOf(), this)
        val factory =
            ProductsViewModelFactory(CollectionProductsRepo(RemoteSource()))
        viewModel = ViewModelProvider(requireActivity(), factory)[ProductsViewModel::class.java]
        return productsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataReceived = requireArguments().getLong("id")
        viewModel.getCollectionProducts(dataReceived)
        productsBinding.productsRV.adapter = productsAdapter
        productsBinding.productsRV.layoutManager = GridLayoutManager(requireContext(), 2)
        filterList = listOf()
        updateRecycleView()
        searchForProduct()
        checkNetwork()
        productsBinding.section0150.setOnClickListener {
            chosePriceFiltration()
        }
        productsBinding.section151300.setOnClickListener {
            chosePriceFiltration()
        }
        productsBinding.sectionAll.setOnClickListener {
            chosePriceFiltration()
        }
    }

    private fun updateRecycleView() {
        lifecycleScope.launch {
            viewModel.collectionProducts.collect {
                when (it) {
                    is ApiState.Loading -> {
                        productsBinding.progressBar2.visibility = View.VISIBLE
                    }
                    is ApiState.Failure -> {
                        productsBinding.progressBar2.visibility = View.GONE
                        print(it.error)
                    }
                    is ApiState.Success<*> -> {
                        var collectionProduct = it.date as CollectProductsModel
                        myProducts = collectionProduct.products
                        getFullProductDetails()
                        //  productsAdapter.updateList(myProducts)
                    }
                }
            }
        }

    }

    fun getFullProductDetails() {
        myProducts.forEach { item -> ids += "${item.id}," }
        viewModel.getSelectedProducts(ids)
        ids = ""
        lifecycleScope.launch {
            viewModel.accessProductList.collect() { result ->
                when (result) {
                    is ApiState.Success<*> -> {
                        productsBinding.progressBar2.visibility = View.GONE
                        var apiProduct = result.date as List<Product>
                        myProducts = apiProduct
                        filterList =myProducts
                        productsAdapter.updateList(myProducts)
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

    fun searchForProduct() {
        productsBinding.productSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    fun chosePriceFiltration(){
        when((productsBinding.priceRadioGroup.checkedRadioButtonId)){
            R.id.section0_150 ->{
                productsAdapter.updateList(filterProductsWithPrice(0,150))
            }
            R.id.section151_300 ->{
                productsAdapter.updateList(filterProductsWithPrice(151,300))

            }
            R.id.sectionAll ->{
                productsAdapter.updateList(myProducts)
            }

        }
    }

    fun filterProductsWithPrice(start: Int, end: Int): List<Product> {
         filterList = myProducts.filter {
            it.variants?.get(0)?.price?.toDouble()
                ?.toInt()!! >= start && it.variants?.get(0)?.price?.toDouble()?.toInt()!! <= end
        }

        return filterList
    }

    fun filterProducts(text: String) {
        var filtteredProducts = mutableListOf<Product>()
        for (product in myProducts) {
            if (product.title?.lowercase()?.contains(text.lowercase())!!) {
                filtteredProducts.add(product)
            }

        }
        productsAdapter.updateList(filtteredProducts)
        if (filtteredProducts.isEmpty()) {
            productsBinding.tvNoProduct.visibility = View.VISIBLE
        } else {
            productsBinding.tvNoProduct.visibility = View.GONE

        }

    }

    override fun ShowProductDetalis(productId: Long) {
        val action = ProductsFragmentDirections.fromProductToDetails(productId)
        Navigation.findNavController(requireView()).navigate(action)
    }

    fun checkNetwork() {
        networkObservation = NetworkConectivityObserver(requireContext())
        lifecycleScope.launch {
            networkObservation.observeOnNetwork().collectLatest {
                when (it.name) {
                    "Avaliavle" -> {
                        Log.i("Internet", it.name)
                        retry()
                    }
                    "Lost" -> {
                        showInternetDialog()
                    }
                    InternetStatus.UnAvailable.name -> {
                        Log.i("Internet", it.name)
                        showInternetDialog()
                    }
                }
            }
        }
    }

    private fun showInternetDialog() {

        (context as MainActivity).showSnakeBar()
    }

    fun retry() {
        val dataReceived = requireArguments().getLong("id")
        viewModel.getCollectionProducts(dataReceived)

    }


}