package com.example.shopify.category.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.R
import com.example.shopify.databinding.FragmentCategoryBinding
import com.example.shopify.mainActivity.MainActivity


class CategoryFragment : Fragment() {
     lateinit var binding : FragmentCategoryBinding
     lateinit var myProducts : List <FakeCategoryModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      var pro1 = FakeCategoryModel("10000",R.drawable.heart)
      var pro2 = FakeCategoryModel("1200",R.drawable.cart)
      var  pro3 = FakeCategoryModel("5000",R.drawable.home)
      var pro4 = FakeCategoryModel("1256",R.drawable.img)

       myProducts = listOf(pro1,pro2,pro3,pro4)
       var myAdapter = CategoryAdapter(myProducts)
      binding.categoryRv.apply {
          adapter = myAdapter
          layoutManager = GridLayoutManager(requireContext(),2)
      }

      var myNewProducts = listOf(pro4,pro3,pro2,pro1)

      binding.radioMen.setOnClickListener {
         myAdapter.updateData(myNewProducts)
      }

      binding.radioWomen.setOnClickListener {
          myAdapter.updateData(myProducts)
      }


    }

    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
    }



}