package com.example.shopify.mainActivity

import VPFragmentAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopify.R
import com.example.shopify.cart.CartFragment
import com.example.shopify.category.view.CategoryFragment
import com.example.shopify.databinding.ActivityMainBinding
import com.example.shopify.detailsScreen.ProductDetailsFragment
import com.example.shopify.favourite.FavouriteFragment
import com.example.shopify.home.HomeFragment
import com.example.shopify.login.LoginFragment
import com.example.shopify.setting.SettingFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var vpFragmentAdapter: VPFragmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val iconList= listOf(
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
            HomeFragment(), CategoryFragment(),CartFragment(), FavouriteFragment(),ProductDetailsFragment()
        ),supportFragmentManager,this.lifecycle)
        binding.vpScreenTitles.adapter=vpFragmentAdapter
        TabLayoutMediator(binding.TabLayoutScreens,binding.vpScreenTitles,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->tab .icon=getDrawable(iconList.get(position))
          //  tab.text = categorysList[position]
            }).attach()
        binding.vpScreenTitles.isUserInputEnabled = false

    }
    override fun onBackPressed() {
        if (binding.vpScreenTitles.getCurrentItem() !== 0) {
            binding.vpScreenTitles.setCurrentItem( binding.vpScreenTitles.getCurrentItem() - 1, false)
        } else {
            finish()

        }
    }
}