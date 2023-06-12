package com.example.shopify.mainActivity

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.shopify.R
import com.example.shopify.databinding.ActivityMainBinding
import com.example.shopify.utiltes.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    //  lateinit var vpFragmentAdapter: VPFragmentAdapter
    lateinit var navController: NavController
    lateinit var configrations: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configrations = getSharedPreferences("Configuration", MODE_PRIVATE)!!


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        setConfigartions()

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
    fun setConfigartions(){
        if (!configrations.contains(Constants.currency)) {
            configrations.edit().putString(Constants.currency,Constants.dollar).apply()
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