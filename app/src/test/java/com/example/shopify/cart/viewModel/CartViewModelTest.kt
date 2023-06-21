package com.example.shopify.cart.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.nework.ApiState
import com.example.shopify.products.viewModel.FakeConcreteProducts
import com.example.shopify.products.viewModel.ProductsViewModel
import com.example.shopify.products.viewModel.responseOfBrandProducts
import com.example.shopify.repo.ICart
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CartViewModelTest{
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var cartViewModel: CartViewModel
    lateinit var repo : ICart
    lateinit var responseCartData: DraftOrderPost

    @Before
    fun setUp(){
        // given -> create objects of repo and view model
        repo = FakeConcreteCart()
        cartViewModel = CartViewModel(repo)
        responseCartData = getCartData()
    }
    @Test
    fun getResponseOfCartDataAndCheckResultAsExpected()= mainDispatcherRule.runBlockingTest{
        //when -> call the method in the view model
        cartViewModel.getCartItems(6969454756162)
        val apiStateSuccess = cartViewModel.accessCartItems.getOrAwaitValue {  } as ApiState.Success<*>
        val result = apiStateSuccess.date as DraftOrderPost
        //then -> Check the result as expected or not
        assertEquals(result.draft_order.id,6969454756162)
        assertEquals(result.draft_order.line_items?.get(0)?.title ,"Custom Item")
    }
}