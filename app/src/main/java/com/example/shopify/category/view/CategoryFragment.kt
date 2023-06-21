package com.example.shopify.category.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.draftOrderCreation.LineItem
import com.example.shopify.Models.productDetails.Product
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.R
import com.example.shopify.category.model.CategoryRepo
import com.example.shopify.category.viewModel.CategoryViewModel
import com.example.shopify.category.viewModel.CategoryViewModelFactory
import com.example.shopify.database.LocalDataSource
import com.example.shopify.databinding.FragmentCategoryBinding
import com.example.shopify.favourite.favViewModel.FavoriteViewModel
import com.example.shopify.favourite.favViewModel.FavoriteViewModelFactory
import com.example.shopify.favourite.model.ConcreteFavClass
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import com.example.shopify.utiltes.LoggedUserData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CategoryFragment : Fragment(),OnClickToShowDetalisOfCategory {
    lateinit var binding: FragmentCategoryBinding
    lateinit var myProducts: List<Product>
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var categoryViewModelFactory: CategoryViewModelFactory
    lateinit var myAdapter : CategoryAdapter
    lateinit var filteredList :List<Product>
    private lateinit var favViewModel : FavoriteViewModel
    private lateinit var favFactory : FavoriteViewModelFactory
    private lateinit var favDraftOrderPost: DraftOrderPost
    private var wishListId :Long?= 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        favFactory = FavoriteViewModelFactory(ConcreteFavClass(RemoteSource(ShopifyAPi.retrofitService)))
        favViewModel =  ViewModelProvider(requireActivity(), favFactory)[FavoriteViewModel::class.java]

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

       if(FirebaseAuth.getInstance().currentUser!=null&&LoggedUserData.favOrderDraft.size == 0){
           lifecycleScope.launch(Dispatchers.Main) {
               delay(500)
               wishListId = LocalDataSource.getInstance().readFromShared(requireContext())?.whiDraftOedredId
               favViewModel.getFavItems(wishListId ?: 0)
           }
           observeOnFav()
       }


        myProducts = listOf()
        filteredList = myProducts
        myAdapter = CategoryAdapter(listOf(),this)
        binding.categoryRv.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        searchForCategory()
        setProductList()
        categoryViewModel.getAllProducts()

        binding.radioShoes.setOnClickListener {
            filterList()
        }

        binding.radioAccessories.setOnClickListener {
            filterList()
        }
        binding.radioShirts.setOnClickListener {
            filterList()
        }
        binding.radioWomen.setOnClickListener {
            filterList()
        }
        binding.radioMen.setOnClickListener {
            filterList()
        }
        binding.radioKid.setOnClickListener {
            filterList()
        }
        binding.radioSale.setOnClickListener {
            disableAllRadioBtns()
            filteredList =myProducts
            myAdapter.updateData(myProducts)
        }



    }


    fun filterList(){
        if (!filterByType().equals("")){
        filteredList = myProducts.filter { it.product_type.equals(filterByType())  && it.tags?.contains(filterByGender()) ?: true  }
        }else{
            filteredList = myProducts.filter {  it.tags?.contains(filterByGender()) ?: true  }

        }
        binding.radioSale.isChecked = false

        myAdapter.updateData(filteredList)
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
    fun filterByGender():String{

        when((binding.rgGender.checkedRadioButtonId)){
            R.id.radio_men ->{
                return " men"
            }
            R.id.radio_kid ->{
                return "kid"
            }
            R.id.radio_women ->{
                return  "women"
            }
            else -> {
                return ""
            }
        }
    }
    fun filterByType():String{

        when((binding.rgAccsesories.checkedRadioButtonId)){
            R.id.radio_accessories ->{
                return "ACCESSORIES"
            }
            R.id.radio_shirts ->{
                return "T-SHIRTS"
            }
            R.id.radio_shoes ->{
                return  "SHOES"
            }
            else -> {
                return ""
            }
        }
    }


    fun filterMyProducts(text:String) {
        var fillterdProducts = mutableListOf<Product>()
        if (ispressAtPutton()) {
            for (product in filteredList) {
                if (product.title?.lowercase()?.contains(text.lowercase())!!) {
                    fillterdProducts.add(product)
                }
            }
            myAdapter.updateData(fillterdProducts)
            if (fillterdProducts.isEmpty()) {
                Toast.makeText(requireContext(), "Sorry,No Data Founded", Toast.LENGTH_SHORT).show()
            }
        } else {
            for (product in myProducts) {
                if (product.title?.lowercase()?.contains(text.lowercase())!!) {
                    fillterdProducts.add(product)
                }
            }
            myAdapter.updateData(fillterdProducts)
            if (fillterdProducts.isEmpty()) {
                Toast.makeText(requireContext(), "Sorry,No Data Founded", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun showDetalisFromCategory(prouctId: Long) {
        val action = CategoryFragmentDirections.fromCategoryToDetalis(prouctId)
        Navigation.findNavController(requireView()).navigate(action)
    }


    fun ispressAtPutton() : Boolean{
        if (binding.radioKid.isChecked || binding.radioAccessories.isChecked || binding.radioMen.isChecked || binding.radioShirts.isChecked
            || binding.radioShoes.isChecked || binding.radioSale.isChecked || binding.radioWomen.isChecked) {
                return true
            }
            return false

    }


    private fun observeOnFav() {
       lifecycleScope.launch {
           favViewModel.favItems.collect{
               when(it){
                   is ApiState.Loading ->{
                     binding.progressBar3.visibility = View.VISIBLE
                   }
                   is  ApiState.Success<*> -> {
                       binding.progressBar3.visibility = View.GONE
                       LoggedUserData.favOrderDraft=
                           ((it.date as? DraftOrderPost)?.draft_order?.line_items ?: mutableListOf()) as MutableList<LineItem>

                   }
                   else ->{
                       binding.progressBar3.visibility = View.GONE
                       Snackbar.make(
                           requireView(),
                           "Failed to obtain data from api",
                           Snackbar.LENGTH_LONG
                       ).show()
                   }
                   }
                }


           }
       }




}