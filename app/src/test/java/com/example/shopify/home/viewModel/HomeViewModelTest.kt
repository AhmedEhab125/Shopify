package com.example.shopify.home.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.brands.BrandModel
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.creatDraftOrderToPostAtApi
import com.example.shopify.createUserToSaveAtApi
import com.example.shopify.nework.ApiState
import com.example.shopify.responseOfCreatingUser
import com.example.shopify.responseOfDraftCreation
import com.example.shopify.signup.signUpViewModel.FakeConcreteRegisterUser
import com.example.shopify.signup.signUpViewModel.SignUpViewModel
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest{

    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var homeViewModel : HomeViewModel
    lateinit var repo : FakeBrandsRepo
    lateinit var fakeResponse : BrandModel

    @Before
    fun setup(){
        // given -> create objects of repo and view model
        repo = FakeBrandsRepo()
        homeViewModel = HomeViewModel(repo)
        fakeResponse = getFakeResponseOfSmartCollections()

    }
    @Test
    fun getBrandsAndCheckTheSizeOfListisEqualOne() = mainDispatcherRule.runBlockingTest {
        //when -> call the method in the view model
        homeViewModel.getBrands()
        val apiState = homeViewModel.accessBrandsList.getOrAwaitValue {  } as ApiState.Success<*>
        val result = apiState.date as BrandModel?

        //then -> Check the result as exepected or not
        assertEquals(result?.smart_collections?.size,1)

    }

    @Test
    fun getbrandsAndCheckTheResultAsExpectedOrNo()= mainDispatcherRule.runBlockingTest {
        //when -> call the method in the view model
        homeViewModel.getBrands()
        val apiState = homeViewModel.accessBrandsList.getOrAwaitValue {  } as ApiState.Success<*>
        val result = apiState.date as BrandModel?

        //then -> Check the result as exepected or not
        assertEquals(result?.smart_collections!![0].id,fakeResponse.smart_collections[0].id)

    }



}