package com.example.shopify.addressPicker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addAddress.Address
import com.example.shopify.R
import com.example.shopify.addressList.model.AddressesRepo
import com.example.shopify.addressList.viewModel.AddressesViewModel
import com.example.shopify.addressList.viewModel.AddressesViewModelFactory
import com.example.shopify.addressPicker.model.AddAddressRepo
import com.example.shopify.addressPicker.viewModel.AddAddressViewModel
import com.example.shopify.addressPicker.viewModel.AddAddressViewModelFactory
import com.example.shopify.databinding.FragmentAddressBinding
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource

class AddressFragment : Fragment() {
lateinit var binding :FragmentAddressBinding
lateinit var addAddressViewModel: AddAddressViewModel
lateinit var addAddressViewModelFactory: AddAddressViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentAddressBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAddressViewModelFactory =
            AddAddressViewModelFactory(AddAddressRepo(RemoteSource(ShopifyAPi.retrofitService)))
        addAddressViewModel = ViewModelProvider(
            requireActivity(),
            addAddressViewModelFactory
        ).get(AddAddressViewModel::class.java)
        binding.btnAddAddress.setOnClickListener {
            var address = AddNewAddress( Address(
                binding.tvAddressLoc.text.toString()
                ,binding.tvCity.text.toString(),binding.tvContryAddress.text.toString(),binding.tvContryAddress.text.toString()
            ))
            addAddressViewModel.addAddresses(address = address)
        }

    }


}