package com.example.shopify.orderHistory.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.Models.orderList.RetriveOrderModel
import com.example.shopify.home.viewModel.FakeBrandsRepo
import com.example.shopify.home.viewModel.HomeViewModel
import com.example.shopify.nework.ApiState
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class OrderListViewModelTest{
    lateinit var orderViewModel : OrderListViewModel
    lateinit var repo : FakeOrderListRepo
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){
        // given -> create objects of repo and view model
        repo = FakeOrderListRepo()
        orderViewModel = OrderListViewModel(repo)

    }
    @Test
    fun getOrderList()= runBlocking{
        orderViewModel.getOrders(12231321)
        val apiState = orderViewModel.accessOrderList.getOrAwaitValue {  } as ApiState.Success<*>
        val result = apiState.date as RetriveOrderModel?

        //then -> Check the result as exepected or not
        assertEquals(result,null)
    }
}