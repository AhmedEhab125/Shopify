package com.example.shopify.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.shopify.R
import com.example.shopify.databinding.FragmentLoginBinding
import com.example.shopify.mainActivity.MainActivity


class LoginFragment : Fragment() {
  lateinit var binding:FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        binding.signUpTv.setOnClickListener {
            //from_login_to_signUp
            Navigation.findNavController(requireView()).navigate(R.id.from_login_to_signUp)
        }
    }


    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(false)
    }

}