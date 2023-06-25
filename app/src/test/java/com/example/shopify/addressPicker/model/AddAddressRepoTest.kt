package com.example.shopify.addressPicker.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.addAddress.AddNewAddress
import com.example.shopify.Models.addressesmodel.AddressesModel
import com.example.shopify.addressPicker.viewModel.createAddressToadd
import com.example.shopify.addressPicker.viewModel.getResponseOfAddNewAddress
import com.example.shopify.repo.FakeAddAddressInterface
import com.example.shopify.repo.IAddCustomerAddress
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
class AddAddressRepoTest {

    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()


    lateinit var fakeRemotSource : IAddCustomerAddress
    lateinit var fakeAddAddress : AddAddressRepo
    lateinit var fakeCreateAddress : AddNewAddress
    lateinit var fakeResponse : AddressesModel

    @Before
    fun setup(){
        fakeRemotSource = FakeAddAddressInterface()
        fakeAddAddress = AddAddressRepo(fakeRemotSource)
        fakeCreateAddress = createAddressToadd()
        fakeResponse = getResponseOfAddNewAddress()
    }


    @Test
    fun addNewAddresAndCheckTheResultIsNotNull() = runBlockingTest{
       val result = fakeAddAddress.addAddress(123456487879,fakeCreateAddress).getOrAwaitValue {  }

        assertThat(result?.addresses,not(nullValue()))

    }

    @Test
    fun addAddressAndcheckTheResultAsExpectedOrNo() = runBlockingTest {

        val result = fakeAddAddress.addAddress(12345648878,fakeCreateAddress).getOrAwaitValue {  }

        assertEquals(result!!.addresses[0].address1,fakeResponse.addresses[0].address1)

    }

}
