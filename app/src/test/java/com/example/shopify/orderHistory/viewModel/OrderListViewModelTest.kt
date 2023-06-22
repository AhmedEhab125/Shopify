package com.example.shopify.orderHistory.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.orderList.RetriveOrderModel
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IOrderList
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
class OrderListViewModelTest  {
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var orderListViewModel: OrderListViewModel
    lateinit var fakeOrderListRepo: IOrderList



   @Before
   fun setUp(){
       fakeOrderListRepo = FakeOrderListRepo()
       orderListViewModel = OrderListViewModel(fakeOrderListRepo)

   }



   @Test
   fun getAllOrderListAndCheckTheResultIsNotNull() = mainDispatcherRule.runBlockingTest {
       orderListViewModel.getOrders(6970326286658)

       val apiState = orderListViewModel.accessOrderList.getOrAwaitValue {  } as ApiState.Loading

       assertThat((apiState.javaClass), not(nullValue()))

   }
}
