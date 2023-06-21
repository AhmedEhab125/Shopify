package com.example.shopify.category.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.detailsScreen.viewModel.FakeConcreteProductDetalis
import com.example.shopify.detailsScreen.viewModel.ProductDetalisViewModel
import com.example.shopify.detailsScreen.viewModel.getProductDetails
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.IAllProducts
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
class CategoryViewModelTest {

    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var fakeCategoryRepo : IAllProducts
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var fakeResponse : CollectProductsModel


    @Before
    fun setup(){
        // given -> create objects of repo and view model
        fakeCategoryRepo = FakeCategoryRepo()
        categoryViewModel = CategoryViewModel(fakeCategoryRepo)
        fakeResponse = fakeResponseOfAllProductsModel()
    }



    @Test
    fun getAllProductsAndCheckTheListNotNull(){
        //when -> call method at view model
        categoryViewModel.getAllProducts()

        val apiStat = categoryViewModel.accessAllProductList.getOrAwaitValue {  } as ApiState.Success<*>

        val result = apiStat.date as CollectProductsModel

        //then -> check the result is not null

        assertThat(result.products,not(nullValue()))
    }


    @Test
    fun getListOfProductsAndCheckTheFirstItemOfList(){
        //when -> call method at view model
        categoryViewModel.getAllProducts()

        val apiStat = categoryViewModel.accessAllProductList.getOrAwaitValue {  } as ApiState.Success<*>

        val result = apiStat.date as CollectProductsModel

        //then -> check the result is not null

        assertEquals(result.products[0].title,fakeResponse.products[0].title)
    }




}