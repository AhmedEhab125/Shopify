package com.example.shopify.signup.sifnUpView

import android.location.Address
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.Models.registrashonModel.Addresse
import com.example.shopify.Models.registrashonModel.Customer
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.databinding.FragmentSignupBinding
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import com.example.shopify.signup.model.ConcreteRegisterUser
import com.example.shopify.signup.signUpViewModel.SignUpViewModel
import com.example.shopify.signup.signUpViewModel.SignUpViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {
lateinit var binding : FragmentSignupBinding
lateinit var auth : FirebaseAuth
lateinit var password : String
lateinit var email : String
lateinit var firstName : String
lateinit var secondName : String
lateinit var address : String
lateinit var city : String
lateinit var country : String
lateinit var phone:String
lateinit var signupViewModel : SignUpViewModel
lateinit var signupFactory : SignUpViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
             binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signupFactory = SignUpViewModelFactory(ConcreteRegisterUser(RemoteSource(ShopifyAPi.retrofitService)))
        signupViewModel = ViewModelProvider(requireActivity(),signupFactory).get(SignUpViewModel::class.java)


        binding.signUpBtn.setOnClickListener {
             email = binding.signEmailTF.text.toString()
             password = binding.signPassTF.text.toString()
             firstName = binding.signFirstNameTF.text.toString()
            secondName = binding.signLastNameTF.text.toString()
            phone = binding.signPhoneTF.text.toString()
            address = binding.signStreetTF.text.toString()
            city = binding.signCityTF.text.toString()
            country = binding.signCounterTF.text.toString()
            if (checkAllFalid()){
                binding.signUpPrograssBar.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                  if (it.isSuccessful){
                      auth.signOut()
                      Toast.makeText(requireContext(),"Sign In Sussessfuly",Toast.LENGTH_SHORT).show()
                      //create User Object And Post It To API
                      creatUser()
                      Log.i("user",firstName + "" + secondName + address + "" + city + "" + country + ""+ phone + "" +email )
                  }else{
                      Toast.makeText(requireContext(),it.exception.toString(),Toast.LENGTH_SHORT).show()
                      Log.i("erorr",it.exception.toString())
                  }
                    binding.signUpPrograssBar.visibility = View.GONE
                }
            }
        }

    }


    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(false)
    }



   private fun checkAllFalid() : Boolean{
        if(binding.signFirstNameTF.text.toString() == ""){
            binding.signFirstNameTFLayout.error = "This Is Required Filed"
            return false
        }
        if(binding.signLastNameTF.text.toString() == ""){
            binding.signLastNameTFLayout.error = "This Is Required Filed"
            return false
        }
       val email = binding.signEmailTF.text
        if(binding.signEmailTF.text.toString() == ""){
           binding.signEmailTFLayout.error = "This Is Required Filed"
           return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.signEmailTFLayout.error = "Check Email Format"
            return false
        }
        if(binding.signPassTF.text.toString() == ""){
            binding.signPassTFLayout.error = "This Is Required Filed"
            binding.signPassTFLayout.errorIconDrawable = null
            return false
        }
        if(binding.signPassTF.length() < 8 ){
            binding.signPassTFLayout.error = "Password Should At Least 8 Character Or Numbers"
            binding.signPassTFLayout.errorIconDrawable = null
            return false
        }
        if(binding.signCongrmPassTF.text.toString() == ""){
            binding.signCongrmPassTFLayout.error = "This Is Required Filed"
            binding.signPassTFLayout.errorIconDrawable = null
            return false
        }
        if(binding.signPassTF.text.toString() != binding.signCongrmPassTF.text.toString()){
            binding.signCongrmPassTFLayout.error = "Passwords Not Matches"
            return false
        }

        if (binding.signStreetTF.text.toString() == ""){
            binding.signStreetTFLayout.error = "This Is Required Filed"
            return false
        }
        if (binding.signCityTF.text.toString() ==""){
            binding.signCityTFLayout.error = "This Is Required Filed"
            return false
        }
        if (binding.signCounterTF.text.toString() == ""){
            binding.signCountryTFLayout.error = "This Is Required Filed"
            return false
        }
        if (binding.signPhoneTF.text.toString() =="" || binding.signPhoneTF.length() != 11){
            binding.signPhoneTFLayout.error = "Please Enter Valid Phone Numder"
            return false
        }

      return true
    }


   private fun creatUser(){
       val address = listOf<Addresse>(Addresse(address1 = address,city,country,null,null,null,null,null))

       val customer = Customer(null,addresses = address,email,firstName,secondName,password,password,phone,null,true)

       val user = CustomerRegistrationModel(customer)
       signupViewModel.registerUserToApi(user)

       lifecycleScope.launch {
           signupViewModel.productInfo.collect{
               when(it){
                   is ApiState.Loading -> {
                       Log.i("Loading","It's Loading")
                     //  binding.progressBar5.visibility = View.VISIBLE
                   }
                   is ApiState.Success<*> ->{
                     val myUser = it.date as CustomerRegistrationModel
                      Log.i("Mizooo",myUser.customer.id.toString())
                   }
                   else -> {
                       val snakbar = Snackbar.make(
                           requireView(),
                           "There Is Failureee",
                           Snackbar.LENGTH_LONG
                       ).setAction("Action", null)
                       snakbar.show()
                   }
               }
           }
       }

   }


}