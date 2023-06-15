package com.example.shopify.cart.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.Models.draftOrderCreation.DraftOrder
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class CartFragment : Fragment(), Communicator {
    private lateinit var cartBinding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartFactory: CartViewModelFactory
    private lateinit var cartViewModel: CartViewModel
    private lateinit var draftOrderPost: DraftOrderPost
    private lateinit var cartItemsList: MutableList<LineItem>
    private var flag = false
    private var draftId: Long? = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cartBinding = FragmentCartBinding.inflate(inflater)
        cartAdapter = CartAdapter(listOf(), this)
        cartFactory = CartViewModelFactory(CartRepo(RemoteSource(ShopifyAPi.retrofitService)))
        draftId = LocalDataSource.getInstance().readFromShared(requireContext())?.cartdraftOrderId
        return cartBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel = ViewModelProvider(requireActivity(), cartFactory)[CartViewModel::class.java]
        cartBinding.cartRV.adapter = cartAdapter
        cartBinding.cartRV.layoutManager = LinearLayoutManager(requireContext())

        if (FirebaseAuth.getInstance().currentUser != null) {
            cartBinding.noData.visibility = View.GONE

            cartViewModel.getCartItems(draftId ?: 0)
            lifecycleScope.launch {
                cartViewModel.accessCartItems.collect {
                    when (it) {
                        is ApiState.Success<*> -> {
                            flag = true
                            cartBinding.cartProgressBar.visibility = View.GONE
                            draftOrderPost = it.date as DraftOrderPost
                            cartItemsList = (draftOrderPost.draft_order.line_items?: mutableListOf()) as MutableList<LineItem>
                            cartAdapter.updateCartList(cartItemsList)
                            calcTotalPrice()

                        }
                        is ApiState.Failure -> {
                            cartBinding.cartProgressBar.visibility = View.GONE
                            Snackbar.make(
                                cartBinding.textView2,
                                "Failed to obtain data from api",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        is ApiState.Loading -> {
                            cartBinding.cartProgressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        } else {
            cartBinding.cartProgressBar.visibility = View.GONE
            cartBinding.totalPrice.visibility = View.GONE
            cartBinding.textView2.visibility = View.GONE
            cartBinding.noData.visibility = View.VISIBLE
            cartBinding.checkoutBtn.visibility = View.GONE
        }
        cartBinding.checkoutBtn.setOnClickListener {
            //    Navigation.findNavController(requireView()).navigate(R.id.from_cart_to_login)
        }


    }

    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
    }

    override fun addItem(position: Int, amount: Int) {
        var quantity = cartItemsList[position].quantity!!
        quantity += amount
        cartItemsList[position].quantity = quantity
        calcTotalPrice()
        cartAdapter.updateCartList(cartItemsList)
    }

    override fun subItem(position: Int, amount: Int) {
        var quantity = cartItemsList[position].quantity!!
        if (quantity > 1) {
            quantity -= amount
        } else {
            deleteDialog(position)
        }
        cartItemsList[position].quantity = quantity
        calcTotalPrice()
        cartAdapter.updateCartList(cartItemsList)
    }

    private fun calcTotalPrice() {
        var count = 0F
        cartItemsList.forEach { item ->
            count += (item.price!!.toFloat()) * (item.quantity!!)
        }
        cartBinding.totalPrice.text = count.toString()
    }

    override fun onPause() {
        super.onPause()
        if(flag){
        if(cartItemsList.size == 0 ){
            var draftOrder2 = LineItem(null, null, "Custome Item", "20.0", null, null, 1,
                null, "Custom Item", null, null, null
            )
            cartItemsList.add(0,draftOrder2)
        }
            draftOrderPost.draft_order.line_items = cartItemsList
            cartViewModel.updateCartItem(draftId!!, draftOrderPost)
        }
    }

    private fun deleteDialog(position: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_dialog)
        dialog.findViewById<Button>(R.id.delete).setOnClickListener {
            cartItemsList.removeAt(position)
            calcTotalPrice()
            cartAdapter.updateCartList(cartItemsList)
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
