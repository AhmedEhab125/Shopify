package com.example.shopify.setting

import android.app.Dialog
import android.content.SharedPreferences
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
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Constraints
import androidx.navigation.Navigation
import com.example.shopify.R
import com.example.shopify.category.view.CategoryFragmentDirections
import com.example.shopify.databinding.FragmentSettingBinding
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.utiltes.Constants


class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding
    lateinit var configrations: SharedPreferences

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
        configrations = activity?.getSharedPreferences("Configuration", AppCompatActivity.MODE_PRIVATE)!!

        createCurrencyDropDownList()
        binding.AboutUscardView.setOnClickListener {
         aboutUstDialog()
        }

        binding.ContactUscardView.setOnClickListener {
            contactUstDialog()
        }
        binding.addressCardView.setOnClickListener {
            goToAddressScreen()
        }


    }
    fun goToAddressScreen(){
        Navigation.findNavController(requireView()).navigate(R.id.action_settingFragment_to_addressListFragment)
    }
    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
    }
    fun createCurrencyDropDownList(){

        binding.tvCurrncy.text = configrations.getString(Constants.currency,Constants.pound)
    binding.CurrncycardView.setOnClickListener {
        currencyDialog()
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
    fun currencyDialog() {
        var dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.currancy_dialog)
        val window: Window? = dialog.getWindow()
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        //window?.setBackgroundDrawableResource(R.color.transparent);
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

         dialog.findViewById<RadioButton>(R.id.poundRadioButton).setOnClickListener {
            configrations.edit().putString(Constants.currency,Constants.pound).apply()
            binding.tvCurrncy.text = configrations.getString(Constants.currency,Constants.pound)
            dialog.dismiss()
        }
        dialog.findViewById<RadioButton>(R.id.dollarRadioButton).setOnClickListener {
            configrations.edit().putString(Constants.currency,Constants.dollar).apply()

            binding.tvCurrncy.text = configrations.getString(Constants.currency,Constants.dollar)
            dialog.dismiss()
        }

    }


}