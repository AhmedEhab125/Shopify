package com.example.shopify.cart.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.cart.viewModel.getCartData
import com.example.shopify.nework.ApiState
import com.example.shopify.products.model.CollectionProductsRepo
import com.example.shopify.products.viewModel.responseOfBrandProducts
import com.example.shopify.repo.FakeCart
import com.example.shopify.repo.FakeProducts
import com.example.shopify.repo.ICart
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CartRepoTest{
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var remoteSource: ICart
    lateinit var repo: CartRepo
    lateinit var productsMock: DraftOrderPost

    @Before
    fun setUp(){
        remoteSource = FakeCart()
        repo = CartRepo(remoteSource)
        productsMock = getCartData()
    }
    @Test
    fun getResponseOfCartDataAndCheckResultAsExpected()= runBlockingTest{
        //when -> call the method in the repo
        repo.getCartItems(6969454756162).collect {
            //then -> Check the result as expected or not
            assertEquals(it!!.draft_order.line_items?.get(0)?.title ,"Custom Item")
        }

    }
}