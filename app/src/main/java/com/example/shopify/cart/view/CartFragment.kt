package com.example.shopify.cart.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.draftOrderCreation.LineItem
import com.example.shopify.R
import com.example.shopify.cart.model.CartRepo
import com.example.shopify.cart.model.Communicator
import com.example.shopify.cart.viewModel.CartViewModel
import com.example.shopify.cart.viewModel.CartViewModelFactory
import com.example.shopify.database.LocalDataSource
import com.example.shopify.databinding.FragmentCartBinding
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import com.example.shopify.utiltes.Constants
import com.example.shopify.utiltes.LoggedUserData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartFragment : Fragment(), Communicator {
    private lateinit var cartBinding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartFactory: CartViewModelFactory
    private lateinit var cartViewModel: CartViewModel
    private lateinit var draftOrderPost: DraftOrderPost
    private var draftId: Long? = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cartBinding = FragmentCartBinding.inflate(inflater)
        cartAdapter = CartAdapter(listOf(), this)
        cartFactory = CartViewModelFactory(CartRepo(RemoteSource()))
        cartViewModel = ViewModelProvider(requireActivity(), cartFactory)[CartViewModel::class.java]
        draftId = LocalDataSource.getInstance().readFromShared(requireContext())?.cartdraftOrderId
        return cartBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartBinding.checkoutBtn.isEnabled = false
        if(LoggedUserData.orderItemsList.size == 0 ){
            cartViewModel.getCartItems(draftId ?: 0)
        }else if (LoggedUserData.orderItemsList.size>1){
            cartBinding.checkoutBtn.isEnabled = true
            Log.i("Order List", "Cart inside else if ${LoggedUserData.orderItemsList.size} ")
        }
        cartBinding.cartRV.adapter = cartAdapter
        cartBinding.cartRV.layoutManager = LinearLayoutManager(requireContext())

        if (FirebaseAuth.getInstance().currentUser != null) {

            lifecycleScope.launch {
                cartViewModel.accessCartItems.collect {
                    when (it) {
                        is ApiState.Loading -> {
                            cartBinding.lottieSplash.visibility = View.GONE
                            cartBinding.cartProgressBar.visibility = View.VISIBLE

                        }
                        is ApiState.Success<*> -> {
                            if(it.date!=null) {

                                cartBinding.cartProgressBar.visibility = View.GONE
                                draftOrderPost = it.date as DraftOrderPost
                                if(LoggedUserData.orderItemsList.size ==0)
                                    LoggedUserData.orderItemsList = (draftOrderPost.draft_order.line_items?: mutableListOf()) as MutableList<LineItem>
                                if(LoggedUserData.orderItemsList.size>1)
                                    cartBinding.checkoutBtn.isEnabled = true
                                showHideAnimation()
                                cartAdapter.updateCartList(LoggedUserData.orderItemsList)
                                calcTotalPrice()
                            }else{
                                cartBinding.cartProgressBar.visibility = View.GONE
                                delay(3000)
                                Snackbar.make(cartBinding.textView2,"Bad internet Connection",Snackbar.LENGTH_LONG).show()
                            }
                        }
                        is ApiState.Failure -> {
                            cartBinding.cartProgressBar.visibility = View.GONE
                            Snackbar.make(
                                cartBinding.textView2,
                                "Failed to obtain data from api",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                    }
                }
            }
        } else {
            cartBinding.cartProgressBar.visibility = View.GONE
            cartBinding.totalPrice.visibility = View.GONE
            cartBinding.textView2.visibility = View.GONE
            cartBinding.lottieSplash.visibility = View.VISIBLE
            cartBinding.lottieMessage.visibility = View.VISIBLE
            cartBinding.lottieMessage.text = "Please log in to view your cart items."
            cartBinding.checkoutBtn.visibility = View.GONE
        }
        cartBinding.checkoutBtn.setOnClickListener {
            val action = CartFragmentDirections.actionCartFragmentToAddressListFragment("cart")
            Navigation.findNavController(requireView()).navigate(action)
        }


    }

    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
    }

    override fun addItem(position: Int, amount: Int) {
        var quantity = LoggedUserData.orderItemsList[position].quantity!!
        quantity += amount
        LoggedUserData.orderItemsList[position].quantity = quantity
        calcTotalPrice()
        cartAdapter.updateCartList(LoggedUserData.orderItemsList)
    }

    override fun subItem(position: Int, amount: Int) {
        var quantity = LoggedUserData.orderItemsList[position].quantity!!
        if (quantity > 1) {
            quantity -= amount
            LoggedUserData.orderItemsList[position].quantity = quantity
            calcTotalPrice()
            cartAdapter.updateCartList(LoggedUserData.orderItemsList)
        } else {
            deleteDialog(position)

        }
    }
    private fun showHideAnimation(){
        if(LoggedUserData.orderItemsList.size ==1){
            cartBinding.lottieSplash.visibility = View.VISIBLE

        }else{
            cartBinding.lottieSplash.visibility = View.GONE
            cartBinding.lottieMessage.visibility = View.GONE
        }
    }
    private fun calcTotalPrice() {
        var count = 0F
        LoggedUserData.orderItemsList.forEach { item ->
            count += (item.price!!.toFloat()) * (item.quantity!!) * Constants.currencyValue
        }
        cartBinding.totalPrice.text = ((count).toInt()).toString() +" ${ Constants.currencyType}"
    }


    private fun deleteDialog(position: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_dialog)
        dialog.findViewById<Button>(R.id.delete).setOnClickListener {
            LoggedUserData.orderItemsList.removeAt(position)
            calcTotalPrice()
            cartAdapter.updateCartList(LoggedUserData.orderItemsList)
            cartBinding.checkoutBtn.isEnabled = false
            showHideAnimation()
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
        }
        val window: Window? = dialog.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }
}
