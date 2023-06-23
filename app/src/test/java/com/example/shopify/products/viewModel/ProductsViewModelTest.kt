package com.example.shopify.products.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.nework.ApiState
import com.example.shopify.payment.model.ThirdPartyResponse
import com.example.shopify.products.model.ICollectionProductsRepo
import com.example.shopify.repo.CollectionProductsInterface
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductsViewModelTest {
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var productsViewModel: ProductsViewModel
    lateinit var repo : ICollectionProductsRepo
    lateinit var responseProduct: CollectProductsModel

    @Before
    fun setUp(){
        // given -> create objects of repo and view model
        repo = FakeConcreteProducts()
        productsViewModel = ProductsViewModel(repo)
        responseProduct = responseOfBrandProducts()
    }
    @Test
    fun getTheResponseOfProductsAndCheckAsExpectedOrNot()= mainDispatcherRule.runBlockingTest{
        //when -> call the method in the view model
        productsViewModel.getCollectionProducts(1010101010101010)
        val apiStateSuccess = productsViewModel.collectionProducts.getOrAwaitValue {  } as ApiState.Success<*>
        val result = apiStateSuccess.date as CollectProductsModel
        //then -> Check the result as expected or not
        assertEquals(result.products[0].id,responseProduct.products[0].id)
        //OR
        assertEquals(result.products[0].id,1010101010101010)
    }
}