package com.example.shopify.addressList.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IAddresses
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressesViewModelTest {


    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var allAddressViewModel : AddressesViewModel
    lateinit var addressRepo : IAddresses
    lateinit var fakeResponse : AddressesModel

    @Before
    fun setUp(){
        addressRepo = FakeAddressesRepo()
        allAddressViewModel = AddressesViewModel(addressRepo)
        fakeResponse = getResponseOfAddNewAddress()
    }


    @Test
    fun getAllListOfAddresses_AndCheckTheArrayNotNull() = mainDispatcherRule.runBlockingTest {
        allAddressViewModel.getAllAddresses(6969454756162)

        val apiState = allAddressViewModel.accessAllAddressesList.getOrAwaitValue {  } as ApiState.Success<*>

        val result = apiState.date as AddressesModel

        assertNotNull(result.addresses)

    }



    @Test
    fun getListOfAddress_AndCheckTheSizeOfArrayEqualFakeResponseSize() = mainDispatcherRule.runBlockingTest {
        allAddressViewModel.getAllAddresses(6969454756162)

        val apiState = allAddressViewModel.accessAllAddressesList.getOrAwaitValue {  } as ApiState.Success<*>

        val result = apiState.date as AddressesModel

        assertEquals(result.addresses.size , fakeResponse.addresses.size)

    }

}