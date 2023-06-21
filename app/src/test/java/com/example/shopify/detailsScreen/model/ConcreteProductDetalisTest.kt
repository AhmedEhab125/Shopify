package com.example.shopify.detailsScreen.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.creatDraftOrderToPostAtApi
import com.example.shopify.createUserToSaveAtApi
import com.example.shopify.detailsScreen.viewModel.getProductDetails
import com.example.shopify.repo.FakeProductDetalisInterface
import com.example.shopify.repo.FakeRegisterUserInterFace
import com.example.shopify.responseOfCreatingUser
import com.example.shopify.responseOfDraftCreation
import com.example.shopify.signup.model.ConcreteRegisterUser
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
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
class ConcreteProductDetalisTest {
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var remoteSource: FakeProductDetalisInterface
    lateinit var detilsRepo : ConcreteProductDetalis
    lateinit var fakeProductDetailsResponse : ProductModel

    @Before
    fun setUp(){
        // when -> crete objects
        remoteSource = FakeProductDetalisInterface()
        detilsRepo = ConcreteProductDetalis(remoteSource)
       fakeProductDetailsResponse = getProductDetails()
    }



    @Test
    fun getProductDetilsListAndCheckResultNotNull()= runBlockingTest {
        // when -> call method at repo
        val result = detilsRepo.getProductDetalis(1231215454).getOrAwaitValue {  }

        // then -> check the result
        assertThat(result,not(nullValue()))
    }


    @Test
    fun registerUserAndCheckTheIDofResponseasExpectedOrNo() = runBlockingTest {
        // when -> call method at repo
        val result = detilsRepo.getProductDetalis(1231215454).getOrAwaitValue {  }
        // then -> check the result
        assertEquals(result?.product?.id,fakeProductDetailsResponse.product?.id)

    }



}