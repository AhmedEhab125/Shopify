package com.example.shopify.favourite.favViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.creatDraftOrderToPostAtApi
import com.example.shopify.createUserToSaveAtApi
import com.example.shopify.nework.ApiState
import com.example.shopify.repo.ICart
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
class FavoriteViewModelTest{

    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()


    lateinit var favViewModel:FavoriteViewModel
    lateinit var fakeFavRepo:ICart
    lateinit var fakeResponse : DraftOrderPost


    @Before
    fun setup(){
        // given -> create objects of repo and view model
        fakeFavRepo = FakeConcreteFavClass()
        favViewModel = FavoriteViewModel(fakeFavRepo)
        fakeResponse = getFakeFavoriteDraftOrde()
    }


    @Test
    fun getFavDraftOrder_AndCheckTheResultNotNull() = mainDispatcherRule.runBlockingTest {
        // when -> call the method at view model
        favViewModel.getFavItems(6969454756162)

        val apiState = favViewModel.favItems.getOrAwaitValue {  } as ApiState.Success<*>

        val result = apiState.date as DraftOrderPost

        //then -> check The Result As Expected Or Not
        assertNotNull(result)

    }

    @Test
    fun getFaveDraftOrder_AndCheckTheResultasExpectedNote() = mainDispatcherRule.runBlockingTest {
        // when -> call the method at view model
        favViewModel.getFavItems(6969454756162)

        val apiState = favViewModel.favItems.getOrAwaitValue {  } as ApiState.Success<*>

        val result = apiState.date as DraftOrderPost

        //then -> check The Result As Expected Or Not
        assertEquals(result.draft_order.note,"WishList")

    }


    @Test
    fun getFaveDraftOrder_AndCheckTheResultasExpected() = mainDispatcherRule.runBlockingTest {
        // when -> call the method at view model
        favViewModel.getFavItems(6969454756162)

        val apiState = favViewModel.favItems.getOrAwaitValue {  } as ApiState.Success<*>

        val result = apiState.date as DraftOrderPost

        //then -> check The Result As Expected Or Not
        assertEquals(result.draft_order,fakeResponse.draft_order)

    }






}