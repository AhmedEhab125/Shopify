package com.example.shopify.detailsScreen.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.shopify.Models.draftOrderCreation.DraftOrder
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.draftOrderCreation.LineItem
import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.R
import com.example.shopify.cart.model.CartRepo
import com.example.shopify.cart.viewModel.CartViewModel
import com.example.shopify.cart.viewModel.CartViewModelFactory
import com.example.shopify.database.LocalDataSource
import com.example.shopify.databinding.FragmentProductDeatilsBinding
import com.example.shopify.detailsScreen.model.ConcreteProductDetalis
import com.example.shopify.detailsScreen.viewModel.ProductDetalisFactory
import com.example.shopify.detailsScreen.viewModel.ProductDetalisViewModel
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class ProductDetailsFragment : Fragment() {
    lateinit var binding: FragmentProductDeatilsBinding
    lateinit var imgAdapter: ImagePagerAdapter
    private lateinit var productDetalisViewModel: ProductDetalisViewModel
    private lateinit var productDetalisFactory : ProductDetalisFactory
    lateinit var myProduct : ProductModel
    private var productIdRecived:Long=0L
    private lateinit var cartFactory:CartViewModelFactory
    private lateinit var cartViewModel:CartViewModel
    var noOfItems = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDeatilsBinding.inflate(layoutInflater)
         cartFactory = CartViewModelFactory(CartRepo(RemoteSource(ShopifyAPi.retrofitService)))
         cartViewModel = ViewModelProvider(requireActivity(), cartFactory)[CartViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productIdRecived = requireArguments().getLong("product_Id")
        /*  imgAdapter = ImagePagerAdapter(requireContext()
              , intArrayOf(R.drawable.headphone,R.drawable.user,R.drawable.cart)
          )*/
        //  binding.imgsViewPager.adapter = imgAdapter
        binding.btnContinue.setOnClickListener {
            if(FirebaseAuth.getInstance().currentUser!=null){
              addToCart()
            }else{
                navToLoginScreen()
            }
        }
        productDetalisFactory = ProductDetalisFactory(ConcreteProductDetalis(RemoteSource(ShopifyAPi.retrofitService)))
        productDetalisViewModel = ViewModelProvider(requireActivity(), productDetalisFactory)[ProductDetalisViewModel::class.java]
        productDetalisViewModel.getProductDetalis(productIdRecived)

        lifecycleScope.launch {
            productDetalisViewModel.productInfo.collect {
                when(it){
                    is ApiState.Loading -> {
                        hideComponantes()
                        binding.progressBar5.visibility = View.VISIBLE
                    }
                    is ApiState.Success<*> ->{
                        myProduct = it.date as ProductModel
                        binding.progressBar5.visibility = View.GONE
                        showComponantes()
                        setData()
                    }
                    else -> {
                        binding.progressBar5.visibility = View.VISIBLE
                        hideComponantes()
                        val snakbar = Snackbar.make(
                            requireView(),
                            "There Is Failureee",
                            Snackbar.LENGTH_LONG
                        ).setAction("Action", null)
                        snakbar.show()
                    }
                }
            }
        }

        binding.noOfItemsTv.text = noOfItems.toString()

        binding.btnIncrementNoItem.setOnClickListener {
            noOfItems++
            binding.noOfItemsTv.text = noOfItems.toString()
        }

        binding.btnDecremenNoItems.setOnClickListener {
            noOfItems--
            if (noOfItems < 1){
                noOfItems=1
                Toast.makeText(requireContext(),"Choose Valid Number",Toast.LENGTH_SHORT).show()
            }

            binding.noOfItemsTv.text = noOfItems.toString()
        }

    }

    private fun addToCart() {
        val draftId =
            LocalDataSource.getInstance().readFromShared(requireContext())?.cartdraftOrderId
        cartViewModel.getCartItems(draftId?:0)
        Log.i("essammmmmm", ""+draftId)
        lifecycleScope.launch{
            cartViewModel.accessCartItems.collect{
                when(it){
                    is ApiState.Success<*>->{
                        var list:MutableList<LineItem> = mutableListOf()
                        list.addAll((it.date as DraftOrderPost).draft_order.line_items?: mutableListOf())
                        Log.i("essammmmm", ""+myProduct.product?.images?.get(0)?.src)
                        val lineItem = LineItem(
                            null,
                            null,
                            null,
                            myProduct.product?.variants?.get(0)?.price,
                            null,
                            null,
                            noOfItems,
                            "${myProduct.product?.id},${myProduct.product?.image?.src}",
                            myProduct.product?.title,
                            null,
                            null,null
                        )
                        list.add(lineItem)
                        val draftOrder = DraftOrderPost(DraftOrder(null,null,list,"CartList",null,draftId))
                        cartViewModel.updateCartItem(draftId?:0,draftOrder)
                    }
                    is ApiState.Failure->{

                    }
                    else->{

                    }
                }
            }
        }

    }


    private fun hideComponantes(){
        binding.imgsViewPager.visibility = View.GONE
        binding.tvProductDetails.visibility = View.GONE
        binding.tvProductName.visibility = View.GONE
        binding.productRatingBar.visibility = View.GONE
        binding.tvProductPrice.visibility = View.GONE
        binding.tvDescription.visibility = View.GONE
        binding.btnContinue.visibility = View.GONE
        binding.btnAddToFav.visibility = View.GONE
        binding.btnDecremenNoItems.visibility = View.GONE
        binding.btnIncrementNoItem.visibility = View.GONE
        binding.noOfItemsTv.visibility = View.GONE
        binding.tvShippingState.visibility = View.GONE
    }


    fun showComponantes(){
        binding.imgsViewPager.visibility = View.VISIBLE
        binding.tvProductDetails.visibility = View.VISIBLE
        binding.tvProductName.visibility = View.VISIBLE
        binding.productRatingBar.visibility = View.VISIBLE
        binding.tvProductPrice.visibility = View.VISIBLE
        binding.tvDescription.visibility = View.VISIBLE
        binding.btnContinue.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.btnDecremenNoItems.visibility = View.VISIBLE
        binding.btnIncrementNoItem.visibility = View.VISIBLE
        binding.noOfItemsTv.visibility = View.VISIBLE
        binding.tvShippingState.visibility = View.VISIBLE
    }


    private fun setData(){
        imgAdapter = ImagePagerAdapter(requireContext(), myProduct.product?.images)
        binding.imgsViewPager.adapter = imgAdapter
        binding.tvProductName.text = myProduct.product?.title
        binding.productRatingBar.rating = 3.5f
        binding.productRatingBar.isEnabled = false
        binding.tvProductPrice.text = myProduct.product?.variants?.get(0)?.price.toString() + " " +"$$"
        binding.tvProductDetails.text = myProduct.product?.body_html // + "jhaskjffkhfkajhfkajhfkjahfkjh kjahfkjahfkajhfkja hfkjafhakjfhkajfhkajfhkahfkjahfkahfkahfkjahfkjahfkjafja kafk"

    }

    private fun navToLoginScreen(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.goto_login_dialog)
        dialog.findViewById<Button>(R.id.ok).setOnClickListener {
            val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToLoginFragment("details",productIdRecived)
            Navigation.findNavController(requireView()).navigate(action)
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
        }
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