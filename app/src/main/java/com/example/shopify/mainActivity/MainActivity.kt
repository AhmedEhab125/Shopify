package com.example.shopify.mainActivity

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.shopify.Models.draftOrderCreation.DraftOrder
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.draftOrderCreation.LineItem
import com.example.shopify.R
import com.example.shopify.cart.model.CartRepo
import com.example.shopify.cart.viewModel.CartViewModel
import com.example.shopify.cart.viewModel.CartViewModelFactory
import com.example.shopify.database.LocalDataSource
import com.example.shopify.databinding.ActivityMainBinding
import com.example.shopify.favourite.favViewModel.FavoriteViewModel
import com.example.shopify.favourite.favViewModel.FavoriteViewModelFactory
import com.example.shopify.favourite.model.ConcreteFavClass
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import com.example.shopify.utiltes.Constants
import com.example.shopify.utiltes.LoggedUserData
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cartFactory: CartViewModelFactory
    private lateinit var cartViewModel: CartViewModel
    private lateinit var configrations: SharedPreferences
    private lateinit var navController: NavController
    private var draftId: Long = 0L
    //  lateinit var vpFragmentAdapter: VPFragmentAdapter
    private lateinit var favViewModel : FavoriteViewModel
    private lateinit var favFactory : FavoriteViewModelFactory
    private var wishListId :Long?= 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartFactory = CartViewModelFactory(CartRepo(RemoteSource(ShopifyAPi.retrofitService)))
        cartViewModel = ViewModelProvider(this, cartFactory)[CartViewModel::class.java]
        draftId = LocalDataSource.getInstance().readFromShared(this)?.cartdraftOrderId ?: 0
        favFactory = FavoriteViewModelFactory(ConcreteFavClass(RemoteSource(ShopifyAPi.retrofitService)))
        favViewModel =  ViewModelProvider(this, favFactory)[FavoriteViewModel::class.java]
        wishListId = LocalDataSource.getInstance().readFromShared(this)?.whiDraftOedredId ?: 0
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configrations = getSharedPreferences("Configuration", MODE_PRIVATE)!!


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        setConfigartions()



    }

    override fun onPause() {
        super.onPause()
        val draftOrder = DraftOrderPost(
            DraftOrder(null, null, LoggedUserData.orderItemsList,
                "CartList", null, draftId)
        )
        cartViewModel.updateCartItem(draftId!!, draftOrder)
        val favDraftOrder = DraftOrderPost(
            DraftOrder(null, null, LoggedUserData.favOrderDraft,
                "WishList", null, wishListId)
        )
        favViewModel.updateFavtItem(wishListId!!,favDraftOrder)
    }


    fun setConfigartions() {
        if (!configrations.contains(Constants.currency)) {
            configrations.edit().putString(Constants.currency, Constants.dollar).apply()
        }else{
            if (configrations.getString(Constants.currency,"").equals(Constants.pound)){
                Constants.currencyValue = 30
                Constants.currencyType = "EGP"
            }

        }
    }


    fun hideNavigationBar(isVisable: Boolean) {
        if (isVisable) {
            binding.bottomNavigationView.visibility = View.VISIBLE
        } else {
            binding.bottomNavigationView.visibility = View.GONE
        }
    }
}