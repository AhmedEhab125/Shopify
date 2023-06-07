package com.example.shopify.setting

import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Constraints
import com.example.shopify.R
import com.example.shopify.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentSettingBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.AboutUscardView.setOnClickListener {
         aboutUstDialog()
        }

        binding.ContactUscardView.setOnClickListener {
            contactUstDialog()
        }


    }





    fun aboutUstDialog() {
        var dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.about_as_dailog)
        val window: Window? = dialog.getWindow()
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        //window?.setBackgroundDrawableResource(R.color.transparent);
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()


    }


    fun contactUstDialog() {
        var dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.contact_us_dailog)
        val window: Window? = dialog.getWindow()
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        //window?.setBackgroundDrawableResource(R.color.transparent);
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

    }



}