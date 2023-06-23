package com.example.shopify.products.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.products.viewModel.responseOfBrandProducts
import com.example.shopify.repo.CollectionProductsInterface
import com.example.shopify.repo.FakeProducts
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectionProductsRepoTest{
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var remoteSource: ICollectionProductsRepo
    lateinit var repo: CollectionProductsRepo
    lateinit var productsMock: CollectProductsModel

    @Before
    fun setUp(){
        remoteSource = FakeProducts()
        repo = CollectionProductsRepo(remoteSource)
        productsMock = responseOfBrandProducts()
    }
    @Test
    fun getTheResponseOfProductsAndCheckAsExpectedOrNot()=runBlockingTest{
        //when -> call the method in the repo
        val response = repo.getCollectionProducts(1010101010101010).getOrAwaitValue {  }
        //then -> Check the result as expected or not
        assertEquals(response!!.products[0].id,productsMock.products[0].id)
        //OR
        assertEquals(response.products[0].id,1010101010101010)
    }
}