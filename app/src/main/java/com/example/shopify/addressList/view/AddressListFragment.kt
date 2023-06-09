package com.example.shopify.addressList.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.Models.orderList.*
import com.example.shopify.Models.postOrderModel.Customer
import com.example.shopify.Models.postOrderModel.LineItem
import com.example.shopify.Models.postOrderModel.PostOrderModel
import com.example.shopify.Models.postOrderModel.ShippingAddress
import com.example.shopify.Models.registrashonModel.Addresse
import com.example.shopify.R
import com.example.shopify.addressList.model.AddressesRepo
import com.example.shopify.addressList.viewModel.AddressesViewModel
import com.example.shopify.addressList.viewModel.AddressesViewModelFactory
import com.example.shopify.database.LocalDataSource
import com.example.shopify.databinding.FragmentAddressListBinding
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddressListFragment : Fragment(),RemoveCustomerAddress {

    lateinit var binding: FragmentAddressListBinding
    lateinit var addressesViewModel: AddressesViewModel
    lateinit var addressesViewModelFactory: AddressesViewModelFactory
    lateinit var myAdapter: AddressListAdapter


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
            AddressesViewModelFactory(AddressesRepo(RemoteSource()))
        addressesViewModel = ViewModelProvider(
            requireActivity(),
            addressesViewModelFactory
        ).get(AddressesViewModel::class.java)
        setRecycleView()
        setAddressList()
        LocalDataSource.getInstance().readFromShared(requireContext())?.userId?.let {
            addressesViewModel.getAllAddresses(
                customerId = it
            )
        }

        binding.btnAddAddresses.setOnClickListener {
            goToAddAddressScreen()
        }


    }


    fun goToAddAddressScreen() {

        Navigation.findNavController(requireView())
            .navigate(R.id.action_addressListFragment_to_addressFragment)
    }

    fun setRecycleView() {
        var data = requireArguments().getString("comesFrom")
        if (data.equals("cart")){
            binding.btnAddAddresses.visibility =View.GONE
            binding.tvAddressHeader.text = "Select Delivery Address"
        }

        myAdapter = AddressListAdapter(mutableListOf(),this,data!!)
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

                            myAdapter.setAddressesList(it.addresses as MutableList<Addresse>)
                        }
                        binding.progressBar4.visibility = View.GONE
                    }
                    is ApiState.Failure -> {
                        binding.progressBar4.visibility = View.GONE

                    }
                    is ApiState.Loading -> {

                    }

                }

            }
        }

    }

    override fun remove(addressId: Long) {
        LocalDataSource.getInstance().readFromShared(requireContext())?.userId?.let {
            addressesViewModel.removeAddress(it,addressId)
        }

    }


    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(false)
    }


}