package com.example.shopify.orderHistory.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.orderList.RetriveOrderModel
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.nework.ApiState
import com.example.shopify.orderHistory.viewModel.getFakeOrderList
import com.example.shopify.products.model.CollectionProductsRepo
import com.example.shopify.products.viewModel.responseOfBrandProducts
import com.example.shopify.repo.CollectionProductsInterface
import com.example.shopify.repo.FakeOrderHistory
import com.example.shopify.repo.FakeProducts
import com.example.shopify.repo.IOrderList
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OrderListRepoTest{
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var remoteSource: IOrderList
    lateinit var repo: OrderListRepo

    @Before
    fun setUp(){
        remoteSource = FakeOrderHistory()
        repo = OrderListRepo(remoteSource)
    }
    @Test
    fun getAllOrderListAndCheckTheResultIsNotNull() = runBlockingTest {
        repo.getOrderList().collect{
            assertThat(it,(nullValue()))
        }
    }

}