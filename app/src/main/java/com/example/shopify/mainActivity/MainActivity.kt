package com.example.shopify.mainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.shopify.R
import com.example.shopify.databinding.ActivityMainBinding
import com.example.shopify.nework.ShopifyAPi
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
  //  lateinit var vpFragmentAdapter: VPFragmentAdapter
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val  bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        NavigationUI.setupWithNavController(bottomNavigationView, navController)

    lifecycleScope.launch (Dispatchers.IO){
        Log.i("getBrands", "${ShopifyAPi.retrofitService.getBrands().body()} ")
    }






    /*  val iconList= listOf(
            R.drawable.home,
            R.drawable.menu,
            R.drawable.cart,
            R.drawable.heart,
            R.drawable.user

        )
        val categorysList= listOf(
            "home",
            "Categories",
            "cart",
            "Favourite",
            "user"

        )


        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vpFragmentAdapter= VPFragmentAdapter(listOf(
            HomeFragment(),CategoryFragment(),
            CartFragment(), FavouriteFragment(),SettingFragment()
        ),supportFragmentManager,this.lifecycle)
        binding.vpScreenTitles.adapter=vpFragmentAdapter
        TabLayoutMediator(binding.TabLayoutScreens,binding.vpScreenTitles,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->tab .icon=getDrawable(iconList.get(position))
          //  tab.text = categorysList[position]
            }).attach()
        binding.vpScreenTitles.isUserInputEnabled = false*/

    }
 /*   override fun onBackPressed() {
        if (binding.vpScreenTitles.getCurrentItem() !== 0) {
            binding.vpScreenTitles.setCurrentItem( binding.vpScreenTitles.getCurrentItem() - 1, false)
        } else {
            finish()

        }
    }*/


    fun hideNavigationBar(isVisable : Boolean) {
        if (isVisable){
            binding.bottomNavigationView.visibility = View.VISIBLE
        }else{
            binding.bottomNavigationView.visibility = View.GONE
        }
    }
}