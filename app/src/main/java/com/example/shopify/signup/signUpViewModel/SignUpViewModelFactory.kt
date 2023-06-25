package com.example.shopify.signup.signUpViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.detailsScreen.viewModel.ProductDetalisViewModel
import com.example.shopify.repo.ProductDetalisInterface
import com.example.shopify.repo.RegisterUserInterFace

class SignUpViewModelFactory  (private val irepo: RegisterUserInterFace) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {

            SignUpViewModel(irepo) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}