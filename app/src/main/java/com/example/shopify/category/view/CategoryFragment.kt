package com.example.shopify.category.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopify.Models.productDetails.Product
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.R
import com.example.shopify.category.model.CategoryRepo
import com.example.shopify.category.viewModel.CategoryViewModel
import com.example.shopify.category.viewModel.CategoryViewModelFactory
import com.example.shopify.databinding.FragmentCategoryBinding
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import kotlinx.coroutines.launch


class CategoryFragment : Fragment() {
    lateinit var binding: FragmentCategoryBinding
    lateinit var myProducts: List<Product>
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var categoryViewModelFactory: CategoryViewModelFactory
    lateinit var myAdapter : CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewModelFactory =
            CategoryViewModelFactory(CategoryRepo(RemoteSource(ShopifyAPi.retrofitService)))
        categoryViewModel = ViewModelProvider(
            requireActivity(),
            categoryViewModelFactory
        ).get(CategoryViewModel::class.java)

        var pro1 = FakeCategoryModel("10000", R.drawable.heart)
        var pro2 = FakeCategoryModel("1200", R.drawable.cart)
        var pro3 = FakeCategoryModel("5000", R.drawable.home)
        var pro4 = FakeCategoryModel("1256", R.drawable.img)

        myProducts = listOf()
        myAdapter = CategoryAdapter(listOf())
        binding.categoryRv.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        var myNewProducts = listOf(pro4, pro3, pro2, pro1)
        setProductList()
        categoryViewModel.getAllProducts()

        binding.radioShoes.setOnClickListener {
            var filteredList = myProducts.filter { it.product_type.equals("SHOES")  }
            myAdapter.updateData(filteredList)
        }

        binding.radioAccessories.setOnClickListener {
            var filteredList = myProducts.filter { it.product_type.equals("ACCESSORIES")  }
            myAdapter.updateData(filteredList)
        }
        binding.radioShirts.setOnClickListener {
            var filteredList = myProducts.filter { it.product_type.equals("T-SHIRTS")  }
            myAdapter.updateData(filteredList)
        }


    }

    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
    }

    fun setProductList() {
        lifecycleScope.launch {
            categoryViewModel.accessAllProductList.collect { result ->
                when (result) {
                    is ApiState.Success<*> -> {

                        var products = result.date as CollectProductsModel?
                        products?.let {
                            myProducts =it.products
                            myAdapter.updateData(it.products )
                        }
                        binding.progressBar3.visibility=View.GONE
                    }
                    is ApiState.Failure -> {
                        binding.progressBar3.visibility=View.GONE

                    }
                    is ApiState.Loading -> {

                    }

                }

            }
        }

    }


}