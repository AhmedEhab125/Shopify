package com.example.shopify.favourite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.R
import com.example.shopify.databinding.FragmentFavouriteBinding
import com.example.shopify.mainActivity.MainActivity

class FavouriteFragment : Fragment() {
    private lateinit var favouriteBinding: FragmentFavouriteBinding
    private lateinit var favAdapter: FavAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favouriteBinding = FragmentFavouriteBinding.inflate(inflater)
        favAdapter = FavAdapter(listOf())
        return favouriteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouriteBinding.favRV.adapter = favAdapter
        favouriteBinding.favRV.layoutManager = GridLayoutManager(requireContext(),2)
    }

    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
    }


}