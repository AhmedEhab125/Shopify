package com.example.shopify.detailsScreen.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
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
import com.example.shopify.favourite.favViewModel.FavoriteViewModel
import com.example.shopify.favourite.favViewModel.FavoriteViewModelFactory
import com.example.shopify.favourite.model.ConcreteFavClass
import com.example.shopify.login.LoginFragment
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import com.example.shopify.utiltes.Constants
import com.example.shopify.utiltes.LoggedUserData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProductDeatilsBinding
    private lateinit var imgAdapter: ImagePagerAdapter
    private lateinit var productDetalisViewModel: ProductDetalisViewModel
    private lateinit var productDetalisFactory: ProductDetalisFactory
    private lateinit var myProduct: ProductModel
    private lateinit var cartFactory: CartViewModelFactory
    private lateinit var cartViewModel: CartViewModel
    private lateinit var favViewModel : FavoriteViewModel
    private lateinit var favFactory : FavoriteViewModelFactory
    private var productIdRecived: Long = 0L
    private var draftId: Long = 0L
    private var noOfItems = 1
    private var wishListId : Long = 0
    private lateinit var jop : Job
  //  private var isFav = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDeatilsBinding.inflate(layoutInflater)
        cartFactory = CartViewModelFactory(CartRepo(RemoteSource(ShopifyAPi.retrofitService)))
        cartViewModel = ViewModelProvider(requireActivity(), cartFactory)[CartViewModel::class.java]
        favFactory = FavoriteViewModelFactory(ConcreteFavClass(RemoteSource(ShopifyAPi.retrofitService)))
        favViewModel =  ViewModelProvider(requireActivity(), favFactory)[FavoriteViewModel::class.java]
        draftId = LocalDataSource.getInstance().readFromShared(requireContext())?.cartdraftOrderId ?:0
        wishListId = LocalDataSource.getInstance().readFromShared(requireContext())?.whiDraftOedredId ?:0
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(LoggedUserData.orderItemsList.size ==0){
            cartViewModel.getCartItems(draftId)
            observeAtGetOrderDraft()
        }
        if (LoggedUserData.favOrderDraft.size == 0){
            favViewModel.getFavItems(wishListId)
            observeAtFavItems()
        }



        productIdRecived = requireArguments().getLong("product_Id")

        binding.btnContinue.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                addToCart()
            } else {
                navToLoginScreen()
            }

        }

        binding.btnAddToFav.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser!=null){
             if (!myProduct.product?.isFav!!){
                addToFav()
                }else{
                    removeFromFav()
             }
            }else{
                navToLoginScreen()
            }
        }

        productDetalisFactory =ProductDetalisFactory(ConcreteProductDetalis(RemoteSource(ShopifyAPi.retrofitService)))
        productDetalisViewModel = ViewModelProvider(requireActivity(),productDetalisFactory)[ProductDetalisViewModel::class.java]
        productDetalisViewModel.getProductDetalis(productIdRecived)

       jop = lifecycleScope.launch {
            productDetalisViewModel.productInfo.collectLatest {
                when (it) {
                    is ApiState.Loading -> {
                        hideComponantes()
                        binding.progressBar5.visibility = View.VISIBLE
                    }
                    is ApiState.Success<*> -> {
                        binding.btnAddToFav.setBackgroundResource(R.drawable.favourite_btn)
                        myProduct = it.date as ProductModel
                        binding.progressBar5.visibility = View.GONE
                        showComponantes()
                        setData()
                        Log.i("Ehabbbbb","Observeation")

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
            if (noOfItems < 1) {
                noOfItems = 1
                Toast.makeText(requireContext(), "Choose Valid Number", Toast.LENGTH_SHORT).show()
            }
            binding.noOfItemsTv.text = noOfItems.toString()
        }
    }

    private fun removeFromFav() {
        val iterator = LoggedUserData.favOrderDraft.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.title == myProduct.product?.title) {
                iterator.remove()
            }
        }
        binding.btnAddToFav.setBackgroundResource(R.drawable.favourite_btn)
        myProduct.product?.isFav = false
        Toast.makeText(requireContext(), "Product Removed From Favorite List", Toast.LENGTH_SHORT).show()

    }


    override fun onPause() {
        super.onPause()
        jop.cancel()

    }

    private fun addToFav() {
        if (isAleradyFav(myProduct.product?.title!!)) {
            Toast.makeText(
                requireContext(),
                "Product is Aleardy in Favorite List",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            binding.btnAddToFav.setBackgroundResource(R.drawable.favorite_clicked)
            val lineItem = LineItem(
                price = myProduct.product?.variants?.get(0)?.price,
                quantity = 1,
                sku = "${myProduct.product?.id},${myProduct.product?.image?.src}",
                title = myProduct.product?.title
            )
            LoggedUserData.favOrderDraft.add(lineItem)
            Toast.makeText(requireContext(), "Product Added To Favorite", Toast.LENGTH_SHORT).show()
            myProduct.product?.isFav = true
        }
    }


    override fun onResume() {
        super.onResume()

    }
    private fun addToCart() {
        var res = isExist(myProduct.product?.title)
        if(res.first){
            LoggedUserData.orderItemsList[res.second].quantity = LoggedUserData.orderItemsList[res.second].quantity?.plus(noOfItems)
        }else{
            val lineItem = LineItem(
                price= myProduct.product?.variants?.get(0)?.price,
                quantity =  noOfItems,
                sku = "${myProduct.product?.id},${myProduct.product?.variants?.get(0)?.id},${myProduct.product?.image?.src}",
                title = myProduct.product?.title
            )
            LoggedUserData.orderItemsList.add(lineItem)
        }
        //Snackbar.make(binding.tvProductDetails,"Item Is Added To Cart",Snackbar.LENGTH_LONG).show()
        Toast.makeText(requireContext(),"Item Is Added To Cart",Toast.LENGTH_LONG).show()
    }
    private fun isExist(title:String?):Pair<Boolean,Int>{
        LoggedUserData.orderItemsList.forEach { item ->
           if(item.title == title){
               return Pair(true,LoggedUserData.orderItemsList.indexOf(item))
           }
        }
        return Pair(false,-1)
    }




    private fun hideComponantes() {
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
    fun showComponantes() {
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
    private fun setData() {
        imgAdapter = ImagePagerAdapter(requireContext(), myProduct.product?.images)
        binding.imgsViewPager.adapter = imgAdapter
        binding.tvProductName.text = myProduct.product?.title
        binding.productRatingBar.rating = 3.5f
        binding.productRatingBar.isEnabled = false
        binding.tvProductPrice.text = "${((myProduct.product?.variants?.get(0)?.price?.toDouble()
            ?.times(Constants.currencyValue)))?.toInt()} ${Constants.currencyType}"

        binding.tvProductDetails.text =
            myProduct.product?.body_html // + "jhaskjffkhfkajhfkajhfkjahfkjh kjahfkjahfkajhfkja hfkjafhakjfhkajfhkajfhkahfkjahfkahfkahfkjahfkjahfkjafja kafk"
        LoggedUserData.favOrderDraft.forEach { item->
            if (item.title == myProduct.product?.title) {
                binding.btnAddToFav.setBackgroundResource(R.drawable.favorite_clicked)
                myProduct.product?.isFav = true
            }
        }
    }
    private fun navToLoginScreen() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.goto_login_dialog)
        dialog.findViewById<Button>(R.id.ok).setOnClickListener {
            val action =
                ProductDetailsFragmentDirections.actionProductDetailsFragmentToLoginFragment(
                    "details",
                    productIdRecived
                )
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
    private fun observeAtGetOrderDraft(){
        lifecycleScope.launch {
            cartViewModel.accessCartItems.collect {
                when (it) {
                    is ApiState.Loading->binding.progressBar5.visibility = View.VISIBLE
                    is ApiState.Success<*> -> {
                        LoggedUserData.orderItemsList.addAll( (it.date as? DraftOrderPost)?.draft_order?.line_items ?: mutableListOf())
                    }
                    is ApiState.Failure -> {
                        Log.i("Failure", "error in get order list in details screen ${it.error.message}")
                    }

                }
            }
        }
    }


    private fun observeAtFavItems() {
        lifecycleScope.launch {
            favViewModel.favItems.collect {
                when (it) {
                    is ApiState.Loading -> binding.progressBar5.visibility = View.VISIBLE
                    is ApiState.Success<*> -> {

                        LoggedUserData.favOrderDraft.addAll(
                            (it.date as? DraftOrderPost)?.draft_order?.line_items ?: mutableListOf()
                        )
                       // hena btdrb ya milad ab2 a3mel check 3aliha
                     /*   LoggedUserData.favOrderDraft.forEach { item->
                            if (item.title == myProduct.product?.title) {
                                binding.btnAddToFav.setBackgroundResource(R.drawable.favorite_clicked)
                                myProduct.product?.isFav = true
                            }
                        }*/
                    }
                    is ApiState.Failure -> {
                        Log.i(
                            "Failure",
                            "error in get order list in details screen ${it.error.message}"
                        )
                    }

                }
            }

        }


    }


    private fun isAleradyFav(name:String) : Boolean{
        LoggedUserData.favOrderDraft.forEach { item->
            if (item.title == name)
              return true
        }
        return false
    }







}