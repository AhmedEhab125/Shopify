package com.example.shopify.signup.signUpViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.creatDraftOrderToPostAtApi
import com.example.shopify.createUserToSaveAtApi
import com.example.shopify.nework.ApiState
import com.example.shopify.responseOfCreatingUser
import com.example.shopify.responseOfDraftCreation
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SignUpViewModelTest{

    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var signViewModel : SignUpViewModel
    lateinit var repo : FakeConcreteRegisterUser
    lateinit var registerUser : CustomerRegistrationModel
    lateinit var responseOfRegister : CustomerRegistrationModel
    lateinit var postDraftOrder : DraftOrderPost
    lateinit var responseOfDraftOrderPost: DraftOrderPost


    @Before
    fun setup(){
        // given -> create objects of repo and view model
        repo = FakeConcreteRegisterUser()
        signViewModel = SignUpViewModel(repo)
        registerUser = createUserToSaveAtApi()
        responseOfRegister = responseOfCreatingUser()
        postDraftOrder = creatDraftOrderToPostAtApi()
        responseOfDraftOrderPost = responseOfDraftCreation()
    }


    @Test
    fun postFakeUserAndCheckTheResponseAsExepectedOrNot() = mainDispatcherRule.runBlockingTest {
        //when -> call the method in the view model
        signViewModel.registerUserToApi(registerUser)

        val apiState = signViewModel.userInfo.getOrAwaitValue {  } as ApiState.Success<*>
        val result = apiState.date as CustomerRegistrationModel

        //then -> Check the result as exepected or not
        assertEquals(result.customer.id,responseOfRegister.customer.id)

    }



    @Test
    fun postDraftDraftOrderAndCheckTheResultAsExepected() = mainDispatcherRule.runBlockingTest {
        //when -> call the method in the view model
        signViewModel.postCartDraftOrder(postDraftOrder)

        val apiState = signViewModel.cartListDraftOrder.getOrAwaitValue {  } as ApiState.Success<*>
        val result = apiState.date as DraftOrderPost
        //then -> Check the result as exepected or not
        assertEquals(result.draft_order.id,responseOfDraftOrderPost.draft_order.id)
    }

    

}