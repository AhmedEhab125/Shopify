package com.example.shopify.favourite.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.constraintlayout.widget.Constraints
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.draftOrderCreation.LineItem
import com.example.shopify.R
import com.example.shopify.database.LocalDataSource
import com.example.shopify.databinding.FragmentFavouriteBinding
import com.example.shopify.favourite.favViewModel.FavoriteViewModel
import com.example.shopify.favourite.favViewModel.FavoriteViewModelFactory
import com.example.shopify.favourite.model.ConcreteFavClass
import com.example.shopify.favourite.model.OnDelete
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import com.example.shopify.utiltes.LoggedUserData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment(),OnDelete {
    private lateinit var favouriteBinding: FragmentFavouriteBinding
    private lateinit var favAdapter: FavAdapter
    private lateinit var favViewModel : FavoriteViewModel
    private lateinit var favFactory : FavoriteViewModelFactory
    private lateinit var favDraftOrderPost: DraftOrderPost
    private var wishListId :Long?= 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favouriteBinding = FragmentFavouriteBinding.inflate(inflater)
        favAdapter = FavAdapter(listOf(),this)
        favFactory = FavoriteViewModelFactory(ConcreteFavClass(RemoteSource(ShopifyAPi.retrofitService)))
        favViewModel =  ViewModelProvider(requireActivity(), favFactory)[FavoriteViewModel::class.java]
        wishListId = LocalDataSource.getInstance().readFromShared(requireContext())?.whiDraftOedredId
        return favouriteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (LoggedUserData.favOrderDraft.size == 0){
            favViewModel.getFavItems(wishListId?:1592654688)
        }

        favouriteBinding.favRV.adapter = favAdapter
        favouriteBinding.favRV.layoutManager = GridLayoutManager(requireContext(),2)

      if(FirebaseAuth.getInstance().currentUser!=null) {
          lifecycleScope.launch {
              favViewModel.favItems.collect {
                  when (it) {
                      is ApiState.Loading -> {
                          favouriteBinding.favLottieSplash.visibility = View.GONE
                          favouriteBinding.favLottieMessage.visibility = View.GONE
                          favouriteBinding.favprogressBar.visibility = View.VISIBLE
                      }
                      is ApiState.Success<*> -> {
                          if (it.date != null) {
                              favouriteBinding.favprogressBar.visibility = View.GONE
                              favDraftOrderPost = it.date as DraftOrderPost
                              if (LoggedUserData.favOrderDraft.size == 0) {
                                  LoggedUserData.favOrderDraft =
                                      (favDraftOrderPost.draft_order.line_items
                                          ?: mutableListOf()) as MutableList<LineItem>
                              }
                              showHiddenAnimation()
                              favAdapter.updateFavList(LoggedUserData.favOrderDraft)
                          }
                      }
                      else -> {
                          favouriteBinding.favprogressBar.visibility = View.GONE
                          Snackbar.make(
                              requireView(),
                              "Failed to obtain data from api",
                              Snackbar.LENGTH_LONG
                          ).show()
                      }
                  }
              }
          }
      }else{
          favouriteBinding.favprogressBar.visibility = View.GONE
          favouriteBinding.favLottieSplash.visibility = View.VISIBLE
          favouriteBinding.favLottieMessage.visibility = View.VISIBLE
          favouriteBinding.favLottieMessage.text = "Please Login To View Your Favorite items"
      }

    }

    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
    }

    override fun deleteFromFav(index: Int) {
        deleteDialog(index)

    }

    private fun deleteDialog(position: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_dialog)
        dialog.findViewById<Button>(R.id.delete).setOnClickListener {
            LoggedUserData.favOrderDraft.removeAt(position)
            favAdapter.updateFavList(LoggedUserData.favOrderDraft)
            showHiddenAnimation()
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



    fun showHiddenAnimation(){
        if(LoggedUserData.favOrderDraft.size ==1){
            favouriteBinding.favLottieSplash.visibility = View.VISIBLE
             favouriteBinding.favLottieMessage.visibility = View.VISIBLE
            favouriteBinding.favLottieMessage.text = "Sorry,There is No Items."
        }else{
            favouriteBinding.favLottieSplash.visibility = View.GONE
            favouriteBinding.favLottieMessage.visibility = View.GONE
        }
    }


}