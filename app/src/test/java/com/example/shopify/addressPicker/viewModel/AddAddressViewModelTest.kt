package com.example.shopify.addressPicker.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.addressList.viewModel.AddressesViewModel
import com.example.shopify.addressList.viewModel.FakeAddressesRepo
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IAddCustomerAddress
import com.example.shopify.repo.IAddresses
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddAddressViewModelTest {


    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var newAddressViewModel : AddAddressViewModel
    lateinit var newaddressRepo : IAddCustomerAddress
    lateinit var fakeResponse : AddressesModel
    lateinit var createAddressToPost : AddNewAddress


    @Before
    fun setUp(){
        newaddressRepo = FakeAddAddressRepo()
        newAddressViewModel = AddAddressViewModel(newaddressRepo)
        createAddressToPost = createAddressToadd()
        fakeResponse = getResponseOfAddNewAddress()
    }


    @Test
    fun addNewAddressAndCheckTheResultNotNull() = mainDispatcherRule.runBlockingTest {
        newAddressViewModel.addAddresses(6969454756162,createAddressToPost)


       val apiState = newAddressViewModel.accessAllAddressesList.getOrAwaitValue {  } as ApiState.Success<*>

        val result = apiState.date as AddressesModel

        assertThat(result.addresses,not(nullValue()))

    }


    @Test
    fun addNewAddressAndCheckTheResultIsAsExpectedOrNot() =  mainDispatcherRule.runBlockingTest {
        newAddressViewModel.addAddresses(6969454756162,createAddressToPost)


        val apiState = newAddressViewModel.accessAllAddressesList.getOrAwaitValue {  } as ApiState.Success<*>

        val result = apiState.date as AddressesModel

        assertEquals(result.addresses[0].address1,fakeResponse.addresses[0].address1)

    }

}