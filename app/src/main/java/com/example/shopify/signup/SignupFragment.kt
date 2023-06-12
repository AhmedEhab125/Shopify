package com.example.shopify.signup

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.shopify.R
import com.example.shopify.databinding.FragmentSignupBinding
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.utiltes.MyValidation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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



    fun checkAllFalid() : Boolean{
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

}