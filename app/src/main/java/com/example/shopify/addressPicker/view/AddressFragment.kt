package com.example.shopify.addressPicker.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addAddress.Address
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.Models.registrashonModel.Addresse
import com.example.shopify.R
import com.example.shopify.addressList.model.AddressesRepo
import com.example.shopify.addressList.view.AddressListFragmentDirections
import com.example.shopify.addressList.viewModel.AddressesViewModel
import com.example.shopify.addressList.viewModel.AddressesViewModelFactory
import com.example.shopify.addressPicker.model.AddAddressRepo
import com.example.shopify.addressPicker.viewModel.AddAddressViewModel
import com.example.shopify.addressPicker.viewModel.AddAddressViewModelFactory
import com.example.shopify.database.LocalDataSource
import com.example.shopify.databinding.FragmentAddressBinding
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddressFragment : Fragment() {
    lateinit var binding: FragmentAddressBinding
    lateinit var addAddressViewModel: AddAddressViewModel
    lateinit var addAddressViewModelFactory: AddAddressViewModelFactory
    lateinit var job: Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater)
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
            checkAddress()
            addAddress()

        }

    }

    fun addAddress() {
        if (binding.tvAddressLoc.text.toString().length > 2
            && binding.tvCity.text.toString().length > 2
            && binding.tvContryAddress.text.toString().length > 3
        ) {
            var address = AddNewAddress(
                Address(
                    binding.tvAddressLoc.text.toString(),
                    binding.tvCity.text.toString(),
                    binding.tvContryAddress.text.toString()
                )
            )
            var userId = LocalDataSource.getInstance().readFromShared(requireContext())?.userId
            userId?.let { userId ->
                addAddressViewModel.addAddresses(
                    customerId = userId,
                    address = address
                )
            }
        } else {
            validAddressDialog()
        }
    }

    fun checkAddress() {
        job = lifecycleScope.launch {
            addAddressViewModel.accessAllAddressesList.collect { result ->
                when (result) {
                    is ApiState.Success<*> -> {

                        var address = result.date as AddressesModel?
                        if (address != null) {
                            Toast.makeText(
                                requireContext(),
                                "Added Successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            val action =
                                AddressFragmentDirections.actionAddressFragmentToAddressListFragment()
                            Navigation.findNavController(requireView()).navigate(action)
                        } else {
                            validAddressDialog()
                        }
                    }
                    is ApiState.Failure -> {
                        failurAddressDialog()
                    }
                    is ApiState.Loading -> {

                    }

                }

            }
        }
    }

    fun validAddressDialog() {
        job.cancel()
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.valid_address_dialog)

        dialog.findViewById<Button>(R.id.cancel).setOnClickListener {
            binding.tvAddressLoc.text?.clear()
            binding.tvCity.text?.clear()
            binding.tvContryAddress.text?.clear()
            dialog.dismiss()
        }
        val window: Window? = dialog.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    fun failurAddressDialog() {
        job.cancel()
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.valid_address_dialog)
        dialog.findViewById<TextView>(R.id.tv_dialog_header).text = "some Thing wrong"
        dialog.findViewById<Button>(R.id.cancel).setOnClickListener {

            dialog.dismiss()
        }
        val window: Window? = dialog.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }


    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(false)
    }

}