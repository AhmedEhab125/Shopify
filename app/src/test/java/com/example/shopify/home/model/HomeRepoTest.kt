package com.example.shopify.home.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.home.viewModel.getFakeResponseOfSmartCollections
import com.example.shopify.orderHistory.model.OrderListRepo
import com.example.shopify.repo.*
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeRepoTest {
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()


    lateinit var fakeremotSource: IBrands
    lateinit var fakeRepo : HomeRepo
    lateinit var fakeBrnadsResponse : BrandModel

    @Before
    fun setUp(){
      fakeremotSource = FakeIBRands()
        fakeRepo = HomeRepo(fakeremotSource)
        fakeBrnadsResponse = getFakeResponseOfSmartCollections()
    }

    @Test
    fun getBrandsAndCheckTheResultIsAsExpectedOrNo() = runBlockingTest {

        val result = fakeRepo.getBrands().getOrAwaitValue {  }

        assertEquals(result!!.smart_collections[0].id,fakeBrnadsResponse.smart_collections[0].id)

    }
}