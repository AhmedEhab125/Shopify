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
import com.example.shopify.mainActivity.MainActivity


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

        createCurrencyDropDownList()
        binding.AboutUscardView.setOnClickListener {
         aboutUstDialog()
        }

        binding.ContactUscardView.setOnClickListener {
            contactUstDialog()
        }


    }
    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
    }
    fun createCurrencyDropDownList(){
        val currencyList = arrayOf("LE", "USD")
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, currencyList)
        binding.currencySpinner.adapter = adapter
        binding.currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Handle item selection here
                binding.tvCurrncy.text = currencyList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
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