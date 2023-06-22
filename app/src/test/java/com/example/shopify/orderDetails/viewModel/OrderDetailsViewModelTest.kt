package com.example.shopify.orderDetails.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.productDetails.Product
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.nework.ApiState
import com.example.shopify.products.viewModel.FakeConcreteProducts
import com.example.shopify.products.viewModel.ProductsViewModel
import com.example.shopify.products.viewModel.responseOfBrandProducts
import com.example.shopify.repo.CollectionProductsInterface
import com.example.shopify.repo.ISelectedProducts
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OrderDetailsViewModelTest{
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var orderDetailsViewModel: OrderDetailsViewModel
    lateinit var repo : ISelectedProducts
    lateinit var responseProduct: CollectProductsModel

    @Before
    fun setUp(){
        // given -> create objects of repo and view model
        repo = FakeConcreteDetailsOrder()
        orderDetailsViewModel = OrderDetailsViewModel(repo)
        responseProduct = getOrderDetails()
    }
    @Test
    fun getTheResponseOfOrderDetailsAndCheckTheResponseAsExpected()= mainDispatcherRule.runBlockingTest{
        //when -> call the method in the view model
        orderDetailsViewModel.getSelectedProducts("8350702338370")
        val apiStateSuccess = orderDetailsViewModel.accessProductList.getOrAwaitValue {  } as ApiState.Success<*>
        val result = apiStateSuccess.date as List<Product>
        //then -> Check the result as expected
        assertEquals(result[0].id,8350702338370)
        assertEquals(result[0].id,responseProduct.products[0].id)

    }
}