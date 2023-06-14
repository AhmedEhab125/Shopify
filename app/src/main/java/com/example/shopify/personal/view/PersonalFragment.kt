package com.example.shopify.personal.view

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.Models.FireBaseModel.MyFireBaseUser
import com.example.shopify.R
import com.example.shopify.database.LocalDataSource
import com.example.shopify.databinding.FragmentPersonalBinding
import com.example.shopify.login.LoginFragmentDirections
import com.example.shopify.mainActivity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


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
        if(FirebaseAuth.getInstance().currentUser==null){
            personalBinding.orderRV.visibility = View.GONE
            personalBinding.whishListRV.visibility = View.GONE
            personalBinding.orderMore.visibility = View.GONE
            personalBinding.whishListMore.visibility = View.GONE
            personalBinding.textView5.visibility = View.GONE
            personalBinding.textView7.visibility = View.GONE
            personalBinding.userName.text = "Anonymous"
            personalBinding.btnLogout.text = "Login"
            personalBinding.btnLogout.setOnClickListener {
                val action = PersonalFragmentDirections.actionPersonalFragmentToLoginFragment("personal",0)
                Navigation.findNavController(requireView()).navigate(action)
            }
        }else{
            val logineUser = LocalDataSource.getInstance().readFromShared(requireContext())
            personalBinding.userName.text = logineUser?.firsName ?: "GEdooooooo"
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
            personalBinding.btnLogout.setOnClickListener {
                auth.signOut()
                LocalDataSource.getInstance().deleteCash(requireContext())
                Navigation.findNavController(requireView()).navigate(R.id.from_logout_to_home)
            }
        }


        personalBinding.settingsBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.from_personal_to_settings)
        }


    }

    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
    }


}