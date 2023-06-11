package com.example.shopify.category.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
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


class CategoryFragment : Fragment(),OnClickToShowDetalisOfCategory {
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

        myProducts = listOf()
        myAdapter = CategoryAdapter(listOf(),this)
        binding.categoryRv.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        searchForCategory()
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

    override fun onPause() {
        super.onPause()
        disableAllRadioBtns()

    }
    fun disableAllRadioBtns(){
        binding.radioShoes.isChecked = false
        binding.radioAccessories.isChecked = false
        binding.radioShirts.isChecked = false
        binding.radioMen.isChecked = false
        binding.radioKid.isChecked = false
        binding.radioWomen.isChecked = false
    }


    fun searchForCategory(){
        binding.categorySearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               filterMyProducts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


    fun filterMyProducts(text:String){
        var fillterdProducts = mutableListOf<Product>()
        for (product in myProducts){
            if (product.title?.lowercase()?.contains(text.lowercase())!!){
                fillterdProducts.add(product)
            }
        }
        myAdapter.updateData(fillterdProducts)
        if (fillterdProducts.isEmpty()){
            Toast.makeText(requireContext(),"Sorry,No Data Founded", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showDetalisFromCategory(prouctId: Long) {
        val action = CategoryFragmentDirections.fromCategoryToDetalis(prouctId)
        Navigation.findNavController(requireView()).navigate(action)
    }


}