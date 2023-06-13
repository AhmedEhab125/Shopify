package com.example.shopify.detailsScreen.view

import android.app.Dialog
import android.os.Bundle
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
import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.R
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
    lateinit var productDetalisViewModel: ProductDetalisViewModel
    lateinit var productDetalisFactory : ProductDetalisFactory
    lateinit var myProduct : ProductModel
    private var productIdRecived:Long=0L
    var noOfItems = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDeatilsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*  imgAdapter = ImagePagerAdapter(requireContext()
              , intArrayOf(R.drawable.headphone,R.drawable.user,R.drawable.cart)
          )*/
        //  binding.imgsViewPager.adapter = imgAdapter
        binding.btnContinue.setOnClickListener {
            if(FirebaseAuth.getInstance().currentUser!=null){
                Toast.makeText(requireContext(),"Added To Cart",Toast.LENGTH_SHORT).show()
            }else{
                navToLoginScreen()
            }
        }
        productDetalisFactory = ProductDetalisFactory(ConcreteProductDetalis(RemoteSource(ShopifyAPi.retrofitService)))

        productDetalisViewModel = ViewModelProvider(requireActivity(), productDetalisFactory).get(ProductDetalisViewModel::class.java)
        productIdRecived = requireArguments().getLong("product_Id")
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



    fun hideComponantes(){
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


    fun setData(){
        imgAdapter = ImagePagerAdapter(requireContext(), myProduct.product?.images)
        binding.imgsViewPager.adapter = imgAdapter
        binding.tvProductName.text = myProduct.product?.title
        binding.productRatingBar.rating = 3.5f
        binding.productRatingBar.isEnabled = false
        binding.tvProductPrice.text = myProduct.product?.variants?.get(0)?.price.toString() + " " +"$$"
        binding.tvProductDetails.text = myProduct.product?.body_html // + "jhaskjffkhfkajhfkajhfkjahfkjh kjahfkjahfkajhfkja hfkjafhakjfhkajfhkajfhkahfkjahfkahfkahfkjahfkjahfkjafja kafk"

    }

    fun navToLoginScreen(){
        var dialog = Dialog(requireContext())
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
        val window: Window? = dialog.getWindow()
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

}