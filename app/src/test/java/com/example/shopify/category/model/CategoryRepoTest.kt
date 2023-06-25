package com.example.shopify.category.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.category.viewModel.fakeResponseOfAllProductsModel
import com.example.shopify.repo.FakeIAllPrducts
import com.example.shopify.repo.IAllProducts
import com.example.shopify.repo.RemoteSource
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
class CategoryRepoTest {
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()


    lateinit var remoteSource: IAllProducts
    lateinit var fakeCategoryRepo : CategoryRepo
    lateinit var fakeResponse : CollectProductsModel

    @Before
    fun setUp(){
        remoteSource = FakeIAllPrducts()
        fakeCategoryRepo = CategoryRepo(remoteSource)
        fakeResponse = fakeResponseOfAllProductsModel()
    }


    @Test
    fun getAllProductsAndCheckTheResultIsNotNull() = runBlockingTest {
      var result =  fakeCategoryRepo.getAllProducts().getOrAwaitValue {  }


       assertThat(result?.products,not(nullValue()))
    }


    @Test
    fun getAllProductsandChecktheProductAsExpected() = runBlockingTest {

        val result = fakeCategoryRepo.getAllProducts().getOrAwaitValue {  }

      assertEquals(result?.products?.get(0)?.id,fakeResponse.products[0].id)
    }


}