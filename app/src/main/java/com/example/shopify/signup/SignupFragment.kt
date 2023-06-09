package com.example.shopify.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.shopify.R
import com.example.shopify.databinding.FragmentSignupBinding
import com.example.shopify.mainActivity.MainActivity

class SignupFragment : Fragment() {
lateinit var binding : FragmentSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        //from_signUp_to_cart
        binding.signUpBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.from_signUp_to_cart)
        }

    }


    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(false)
    }

}