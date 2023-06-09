package com.example.shopify.login

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.shopify.R
import com.example.shopify.database.UserFireBaseDataBase
import com.example.shopify.databinding.FragmentLoginBinding
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.utiltes.LoggedUserData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    lateinit var binding:FragmentLoginBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val goto = requireArguments().getString("from")
        binding.signUpTv.setOnClickListener {
            //from_login_to_signUp
            val action = LoginFragmentDirections.fromLoginToSignUp(goto!!,requireArguments().getLong("id"))
            Navigation.findNavController(requireView()).navigate(action)
        }
        binding.loginBtn.setOnClickListener {
            val email = binding.loginEmailTF.text.toString()
            val password = binding.loginPassTF.text.toString()
            if (isDataValid()){
                binding.loginProgressBar.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if (it.isSuccessful){
                        UserFireBaseDataBase.getUserFromFireBase(requireContext(),auth.currentUser!!)
                        LoggedUserData.favOrderDraft.clear()
                        LoggedUserData.orderItemsList.clear()
                        lifecycleScope.launch(Dispatchers.Main){
                            delay(500)
                            Toast.makeText(requireContext(),"Log in Sussessfuly",Toast.LENGTH_SHORT).show()
                            binding.loginProgressBar.visibility = View.GONE
                            when (goto){
                                "details" -> {
                                    val id = requireArguments().getLong("id")
                                    val action = LoginFragmentDirections.actionLoginFragmentToProductDetailsFragment(id)
                                    Navigation.findNavController(requireView()).navigate(action)
                                }
                                "personal"-> Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
                                "category" ->Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment) //fromLoginToCategory
                            }
                        }

                    }else{
                        Toast.makeText(requireContext(),it.exception.toString(),Toast.LENGTH_SHORT).show()
                        binding.loginProgressBar.visibility = View.GONE
                    }

                }
            }

        }

        binding.forgetPassTF.setOnClickListener {
            val email = binding.loginEmailTF.text.toString()
            if (checkDataForForgetPasswordIsValid()){
               auth.sendPasswordResetEmail(email).addOnSuccessListener {
                   Toast.makeText(requireContext(),"Please Check Your Email",Toast.LENGTH_SHORT).show()
               }
                   .addOnFailureListener {
                       Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
                   }

            }
        }

    }


    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(false)
    }



    private fun isDataValid() : Boolean {
        val email = binding.loginEmailTF.text
        if(binding.loginEmailTF.text.toString() == ""){
            binding.loginEmailTFLayout.error = "This Is Required Filed"
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.loginEmailTFLayout.error = "Check Email Format"
            return false
        }
        if(binding.loginPassTF.text.toString() == ""){
            binding.loginPassTFLayout.error = "This Is Required Filed"
            binding.loginPassTFLayout.errorIconDrawable = null
            return false
        }
        if(binding.loginPassTF.length() < 8 ){
            binding.loginPassTFLayout.error = "Password Should At Least 8 Character Or Numbers"
            binding.loginPassTFLayout.errorIconDrawable = null
            return false
        }

        return true
    }



    private fun checkDataForForgetPasswordIsValid() : Boolean{
        val email = binding.loginEmailTF.text
        if(binding.loginEmailTF.text.toString() == ""){
            binding.loginEmailTFLayout.error = "This Is Required Filed"
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.loginEmailTFLayout.error = "Check Email Format"
            return false
        }
        return true
    }




}