package com.example.shopify.detailsScreen.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.ProductDetalisInterface
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductDetalisViewModelTest{

    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var fakeRepo : ProductDetalisInterface
    lateinit var productDetailsViewModel : ProductDetalisViewModel
    lateinit var fakeResponse : ProductModel


    @Before
    fun setup(){
        // given -> create objects of repo and view model
        fakeRepo = FakeConcreteProductDetalis()
        productDetailsViewModel = ProductDetalisViewModel(fakeRepo)
        fakeResponse = getProductDetails()
    }



   @Test
   fun getProductDetailsForFakeId_AndCheckTheResultNotNull(){
       // when -> call method at viewModel
       productDetailsViewModel.getProductDetalis(6969454756162)

       val apiState = productDetailsViewModel.productInfo.getOrAwaitValue {  } as ApiState.Success<*>
       val result = apiState.date as ProductModel

       //then -> Check The Result AS Expected or No

       assertThat(result,not(nullValue()))

   }


    @Test
    fun getProductDetailsForFakeId_AndCheckTheExepectedResult(){
        // when -> call method at viewModel
        productDetailsViewModel.getProductDetalis(6969454756162)

        val apiState = productDetailsViewModel.productInfo.getOrAwaitValue {  } as ApiState.Success<*>
        val result = apiState.date as ProductModel

        //then -> Check The Result AS Expected or No

        assertEquals(result.product?.id,fakeResponse.product?.id)

    }

}