package com.example.shopify.home.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.shopify.R
import com.example.shopify.databinding.FragmentHomeBinding
import java.lang.Math.abs

class HomeFragment : Fragment(){
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var brandsAdapter: BrandsAdapter
    private lateinit var handler: Handler
    private lateinit var adsImages:ArrayList<Int>
    private lateinit var adsAdapter: AdsAdapter
    private lateinit var viewPager2: ViewPager2
    private val runnable =Runnable {
        viewPager2.currentItem = viewPager2.currentItem+1
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater)
        //homeBinding.homeSearch
        viewPager2 = homeBinding.dicountsSlider
        init()
        setUpTarnsformer()
        viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable,2000)
            }
        })

        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        brandsAdapter = BrandsAdapter(listOf())
        homeBinding.brandsRV.adapter = brandsAdapter
        homeBinding.brandsRV.layoutManager = GridLayoutManager(requireContext(),2)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,3000)
    }
    private fun init(){
        handler = Handler(Looper.myLooper()!!)
        adsImages = ArrayList()
        adsImages.add(R.drawable.headphone)
        adsImages.add(R.drawable.home)
        adsImages.add(R.drawable.cart)
        adsImages.add(R.drawable.user)
        adsImages.add(R.drawable.heart)
        adsAdapter = AdsAdapter(adsImages,viewPager2)
        homeBinding.dicountsSlider.adapter = adsAdapter
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }
    private fun setUpTarnsformer() {
       val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->

            page.scaleY = 0.85f
        }
        viewPager2.setPageTransformer(transformer)
    }

}