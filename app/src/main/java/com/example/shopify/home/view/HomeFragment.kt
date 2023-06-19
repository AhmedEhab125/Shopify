package com.example.shopify.home.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.Models.brands.SmartCollection
import com.example.shopify.R
import com.example.shopify.databinding.FragmentHomeBinding
import com.example.shopify.home.model.Ads
import com.example.shopify.home.model.HomeRepo
import com.example.shopify.home.viewModel.HomeViewModel
import com.example.shopify.home.viewModel.HomeViewModelFactory
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import com.example.shopify.utiltes.Constants
import com.example.shopify.utiltes.LoggedUserData
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var brandsAdapter: BrandsAdapter
    private lateinit var handler: Handler
    private lateinit var ads: ArrayList<Ads>
    private lateinit var adsAdapter: AdsAdapter
    private lateinit var viewPager2: ViewPager2
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var smartCollections : List<SmartCollection>
    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        smartCollections = listOf()
        homeViewModelFactory = HomeViewModelFactory(HomeRepo(RemoteSource(ShopifyAPi.retrofitService)))
        homeViewModel = ViewModelProvider(
            requireActivity(),
            homeViewModelFactory
        ).get(HomeViewModel::class.java)
        homeBinding = FragmentHomeBinding.inflate(inflater)
        //homeBinding.homeSearch
        viewPager2 = homeBinding.dicountsSlider
        init()
        setUpTarnsformer()
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 5000)
            }
        })

        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        brandsAdapter = BrandsAdapter(listOf())
        homeBinding.brandsRV.adapter = brandsAdapter
        homeBinding.brandsRV.layoutManager = GridLayoutManager(requireContext(), 2,RecyclerView.HORIZONTAL, false)
        setBrandData()
        homeViewModel.getBrands()
        searchForBrands()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
        handler.postDelayed(runnable, 5000)

    }

    private fun init() {
        handler = Handler(Looper.myLooper()!!)
        ads = ArrayList()
        ads.add(Ads(R.drawable.discount1,Constants.vouchersList[0]))
        ads.add(Ads(R.drawable.discount2,Constants.vouchersList[1]))
        ads.add(Ads(R.drawable.discount3,Constants.vouchersList[2]))
        ads.add(Ads(R.drawable.discount4,Constants.vouchersList[3]))
        ads.add(Ads(R.drawable.discount5,Constants.vouchersList[4]))
        ads.add(Ads(R.drawable.discount6,Constants.vouchersList[5]))
        adsAdapter = AdsAdapter(ads, viewPager2)
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

    fun setBrandData() {
        lifecycleScope.launch {
            homeViewModel.accessBrandsList.collect() { result ->
                when (result) {
                    is ApiState.Success<*> -> {
                        homeBinding.progressBar.visibility = View.GONE

                        var brands = result.date as BrandModel?
                        smartCollections = brands?.smart_collections ?: listOf()
                        brandsAdapter.setBrandsList(smartCollections)
                    }
                    is ApiState.Failure -> {
                        homeBinding.progressBar.visibility = View.GONE

                    }
                    is ApiState.Loading -> {
                        homeBinding.progressBar.visibility = View.VISIBLE
                    }

                }

            }
        }
    }


    fun searchForBrands(){
        homeBinding.homeSearch.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                filterBrands(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }



    fun filterBrands(text:String){
        var filterdBrands = mutableListOf<SmartCollection>()
        for(brand in smartCollections){
            if (brand.title.lowercase().contains(text.lowercase()) ) {
               filterdBrands.add(brand)
            }

        }
        brandsAdapter.setBrandsList(filterdBrands)
        if (filterdBrands.isEmpty()){
            Toast.makeText(requireContext(),"Sorry,No Data Founded",Toast.LENGTH_SHORT).show()
        }

    }


}