package com.example.shopify.addressList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopify.R
import com.example.shopify.databinding.FragmentAddressListBinding


class AddressListFragment : Fragment() {

    lateinit var binding : FragmentAddressListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressListBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }


}