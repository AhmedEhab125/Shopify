package com.example.shopify.addressList.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.Models.registrashonModel.Addresse
import com.example.shopify.R
import com.example.shopify.addressList.model.AddressesRepo
import com.example.shopify.addressList.viewModel.AddressesViewModel
import com.example.shopify.addressList.viewModel.AddressesViewModelFactory
import com.example.shopify.category.model.CategoryRepo
import com.example.shopify.category.view.CategoryAdapter
import com.example.shopify.category.viewModel.CategoryViewModel
import com.example.shopify.category.viewModel.CategoryViewModelFactory
import com.example.shopify.databinding.FragmentAddressListBinding
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import kotlinx.coroutines.launch


class AddressListFragment : Fragment() {

    lateinit var binding : FragmentAddressListBinding
    lateinit var addressesViewModel: AddressesViewModel
    lateinit var addressesViewModelFactory: AddressesViewModelFactory
    lateinit var myAdapter : AddressListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressListBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addressesViewModelFactory =
            AddressesViewModelFactory(AddressesRepo(RemoteSource(ShopifyAPi.retrofitService)))
        addressesViewModel = ViewModelProvider(
            requireActivity(),
            addressesViewModelFactory
        ).get(AddressesViewModel::class.java)
        setRecycleView()
        setAddressList()
        addressesViewModel.getAllAddresses()

    }
    fun setRecycleView(){
        myAdapter = AddressListAdapter(listOf())
        binding.rvAddresses.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    fun setAddressList() {
        lifecycleScope.launch {
            addressesViewModel.accessAllAddressesList.collect { result ->
                when (result) {
                    is ApiState.Success<*> -> {

                        var products = result.date as AddressesModel?
                        products?.let {

                            myAdapter.setAddressesList(it.addresses)
                        }
                        binding.progressBar4.visibility=View.GONE
                    }
                    is ApiState.Failure -> {
                       binding.progressBar4.visibility=View.GONE

                    }
                    is ApiState.Loading -> {

                    }

                }

            }
        }

    }


}