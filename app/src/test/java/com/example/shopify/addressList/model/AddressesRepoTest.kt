package com.example.shopify.addressList.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.addressList.viewModel.getResponseOfAddNewAddress
import com.example.shopify.repo.FakeIAddresses
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
class AddressesRepoTest {
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var fakeRemote : IAddresses
    lateinit var fakeAddressesRepo: AddressesRepo
    lateinit var fakeResponse : AddressesModel


    @Before
    fun setUp(){
        fakeRemote = FakeIAddresses()
        fakeAddressesRepo = AddressesRepo(fakeRemote)
        fakeResponse = getResponseOfAddNewAddress()
    }


    @Test
    fun getAllAddressOfCustomerAndCheckTheResultNotNull() = runBlockingTest {
      val result =  fakeAddressesRepo.getAddresses(123456498).getOrAwaitValue {  }

      assertThat(result?.addresses,not(nullValue()))
    }

    @Test
    fun getListOfAddressOfCoustomerAndCheckTheRusltAsExpexted() = runBlockingTest {
        val result = fakeAddressesRepo.getAddresses(1236549856).getOrAwaitValue {  }

        assertEquals(result!!.addresses[0].address1,fakeResponse.addresses[0].address1)
    }

    @Test
    fun getAllAddressAndCheckTheSizeOftheArray() = runBlockingTest {
        val result = fakeAddressesRepo.getAddresses(12365477895).getOrAwaitValue {  }

        assertEquals(result!!.addresses.size,2)
    }
}