package com.example.shopify.detailsScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopify.R
import com.example.shopify.databinding.FragmentProductDeatilsBinding


class ProductDetailsFragment : Fragment() {
    lateinit var binding: FragmentProductDeatilsBinding
    lateinit var imgAdapter: ImagePagerAdapter
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
        imgAdapter = ImagePagerAdapter(requireContext()
            , intArrayOf(R.drawable.headphone,R.drawable.user,R.drawable.cart)
        )
        binding.imgsViewPager.adapter = imgAdapter
    }


}