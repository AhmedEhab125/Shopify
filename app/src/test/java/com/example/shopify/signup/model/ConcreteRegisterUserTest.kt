package com.example.shopify.signup.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.creatDraftOrderToPostAtApi
import com.example.shopify.createUserToSaveAtApi
import com.example.shopify.repo.FakeRegisterUserInterFace
import com.example.shopify.responseOfCreatingUser
import com.example.shopify.responseOfDraftCreation
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.internal.bytebuddy.implementation.InvokeDynamic.InvocationProvider.ArgumentProvider.ForNullValue
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConcreteRegisterUserTest {
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var remoteSource: FakeRegisterUserInterFace
    lateinit var userRepo : ConcreteRegisterUser
    lateinit var fakeRegitserUser : CustomerRegistrationModel
    lateinit var responseOfRegisterUser : CustomerRegistrationModel
    lateinit var wishListDraftOrder : DraftOrderPost
    lateinit var responseOfWishListDraftOrderPost: DraftOrderPost
    @Before
    fun setUp(){
      // when -> crete objects
        remoteSource = FakeRegisterUserInterFace()
        userRepo = ConcreteRegisterUser(remoteSource)
        fakeRegitserUser = createUserToSaveAtApi()
        responseOfRegisterUser = responseOfCreatingUser()
        wishListDraftOrder = creatDraftOrderToPostAtApi()
        responseOfWishListDraftOrderPost = responseOfDraftCreation()
    }



    @Test
    fun registerNewUser_AndCheckTheUserAsEXpectedOrNo()= runBlockingTest {
       // when -> call method at repo
      val result = userRepo.createUserAtApi(fakeRegitserUser)

       val acutal = flowOf(responseOfRegisterUser)

      // then -> check the result
       assertEquals(result.toList(),acutal.toList())
    }


    @Test
    fun registerUserAndCheckTheIDofResponseasExpectedOrNo() = runBlockingTest {
       // when -> call method at repo
       val result = userRepo.createUserAtApi(fakeRegitserUser).getOrAwaitValue {  }
        // then -> check the result
       assertEquals(result?.customer?.id,responseOfRegisterUser.customer.id)

    }


    @Test
    fun createAnDraftOrderAndCheckTheResultNotNull() = runBlockingTest {
        //when -> call method at repo

        val result = userRepo.createWishDraftOrder(wishListDraftOrder).getOrAwaitValue {  }
       // then -> check the result
        assertThat(result,not(nullValue()))
    }


    @Test
    fun createDraftOrderandCheckTheResultAsExpected() = runBlockingTest {
        //when -> call method at repo

        val result = userRepo.createWishDraftOrder(wishListDraftOrder).getOrAwaitValue {  }
        // then -> check the result
      assertEquals(result?.draft_order?.id,responseOfWishListDraftOrderPost.draft_order.id)


    }


}