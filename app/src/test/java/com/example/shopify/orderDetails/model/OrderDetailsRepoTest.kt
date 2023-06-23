package com.example.shopify.orderDetails.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.orderDetails.viewModel.getOrderDetails
import com.example.shopify.repo.FakeOrderDetails
import com.example.shopify.repo.ISelectedProducts
import com.example.wetharapplication.MainDispatcherRule
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OrderDetailsRepoTest{
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var remoteSource: ISelectedProducts
    lateinit var repo:OrderDetailsRepo
    lateinit var orderDetailsMock: CollectProductsModel

    @Before
    fun setUp(){
       remoteSource = FakeOrderDetails()
        repo = OrderDetailsRepo(remoteSource)
        orderDetailsMock =  getOrderDetails()
    }

    @Test
    fun getTheResponseOfOrderDetailsAndCheckTheResponseAsExpected()= runBlockingTest{
        //when -> call the method in the repo
         repo.getSelectedProducts("8350702338370").collect{
            // then -> check the result
            assertEquals(it!!.products[0].id,orderDetailsMock.products[0].id)
        }
    }
}