package com.example.shopify.personal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.R
import com.example.shopify.databinding.FragmentPersonalBinding
import com.example.shopify.mainActivity.MainActivity
import com.google.firebase.auth.FirebaseAuth


class PersonalFragment : Fragment() {
    private lateinit var personalBinding: FragmentPersonalBinding
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var wishListAdapter: WishListAdapter
    lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personalBinding = FragmentPersonalBinding.inflate(inflater)
        ordersAdapter = OrdersAdapter(listOf())
        wishListAdapter = WishListAdapter(listOf())
        return personalBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personalBinding.orderRV.adapter = ordersAdapter
        personalBinding.orderRV.layoutManager = GridLayoutManager(requireContext(),2)
        personalBinding.whishListRV.adapter = wishListAdapter
        personalBinding.whishListRV.layoutManager = GridLayoutManager(requireContext(),2)
        personalBinding.orderMore.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_personalFragment_to_orderListFragment)
        }
        personalBinding.whishListMore.setOnClickListener {
            print("more of WishList")
        }

        personalBinding.settingsBtn.setOnClickListener {
            //from_personal_to_settings
            Navigation.findNavController(requireView()).navigate(R.id.from_personal_to_settings)
        }

        personalBinding.btnLogout.setOnClickListener {
            auth.signOut()
            // ha3ml fnish ll app wala l2 ??
        }
    }

    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
    }


}