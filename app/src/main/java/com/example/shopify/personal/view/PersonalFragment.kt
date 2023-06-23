package com.example.shopify.personal.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopify.Models.draftOrderCreation.DraftOrder
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.draftOrderCreation.LineItem
import com.example.shopify.Models.orderList.Order
import com.example.shopify.R
import com.example.shopify.cart.model.CartRepo
import com.example.shopify.cart.viewModel.CartViewModel
import com.example.shopify.cart.viewModel.CartViewModelFactory
import com.example.shopify.ckeckNetwork.InternetStatus
import com.example.shopify.ckeckNetwork.NetworkConectivityObserver
import com.example.shopify.ckeckNetwork.NetworkObservation
import com.example.shopify.database.LocalDataSource
import com.example.shopify.databinding.FragmentPersonalBinding
import com.example.shopify.favourite.favViewModel.FavoriteViewModel
import com.example.shopify.favourite.favViewModel.FavoriteViewModelFactory
import com.example.shopify.favourite.model.ConcreteFavClass
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.orderHistory.model.OrderListRepo
import com.example.shopify.orderHistory.viewModel.OrderListViewModel
import com.example.shopify.orderHistory.viewModel.OrderListViewModelFactory
import com.example.shopify.repo.RemoteSource
import com.example.shopify.utiltes.LoggedUserData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class PersonalFragment : Fragment() {
    private lateinit var personalBinding: FragmentPersonalBinding
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var wishListAdapter: WishListAdapter
    lateinit var auth : FirebaseAuth
    private lateinit var cartFactory: CartViewModelFactory
    private lateinit var cartViewModel: CartViewModel
    private var draftId: Long = 0L
    private lateinit var favViewModel : FavoriteViewModel
    private lateinit var favFactory : FavoriteViewModelFactory
    private var wishListId :Long?= 0L
    private lateinit var favDraftOrderPost: DraftOrderPost
    lateinit var orderListViewModel: OrderListViewModel
    lateinit var orderListViewModelFactory: OrderListViewModelFactory
     var userId :Long = 0L
    lateinit var networkObservation: NetworkObservation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        personalBinding = FragmentPersonalBinding.inflate(inflater)
        ordersAdapter = OrdersAdapter(listOf())
        wishListAdapter = WishListAdapter(listOf())
        favFactory = FavoriteViewModelFactory(ConcreteFavClass(RemoteSource()))
        favViewModel =  ViewModelProvider(requireActivity(), favFactory)[FavoriteViewModel::class.java]
        wishListId = LocalDataSource.getInstance().readFromShared(requireContext())?.whiDraftOedredId ?: 0
        cartFactory = CartViewModelFactory(CartRepo(RemoteSource()))
        cartViewModel = ViewModelProvider(requireActivity(), cartFactory)[CartViewModel::class.java]
        draftId = LocalDataSource.getInstance().readFromShared(requireContext())?.cartdraftOrderId ?: 0
        userId = LocalDataSource.getInstance().readFromShared(requireContext())?.userId ?:0L
        return personalBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderListViewModelFactory =  OrderListViewModelFactory(
            OrderListRepo(
            RemoteSource()
        )
        )
        orderListViewModel = ViewModelProvider(
            requireActivity(),
            orderListViewModelFactory
        ).get(OrderListViewModel::class.java)
        if (LoggedUserData.favOrderDraft.size == 0){
            favViewModel.getFavItems(wishListId?:1592654688)

        }
        checkNetwork()
        if(FirebaseAuth.getInstance().currentUser==null){
            personalBinding.tvNoFav.visibility =View.GONE
            personalBinding.tvNoOrders.visibility =View.GONE

            personalBinding.view2.visibility =View.GONE
            personalBinding.view3.visibility =View.GONE
            personalBinding.personProgressBar.visibility = View.GONE
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
            observeOnFavItems()
            getOrders()
            personalBinding.whishListRV.layoutManager = GridLayoutManager(requireContext(),2)
            personalBinding.orderMore.setOnClickListener {
                Navigation.findNavController(requireView()).navigate(R.id.action_personalFragment_to_orderListFragment)
            }
            personalBinding.whishListMore.setOnClickListener {
               Navigation.findNavController(requireView()).navigate(R.id.from_person_to_fav)
            }
            personalBinding.btnLogout.setOnClickListener {

                val draftOrder = DraftOrderPost(
                    DraftOrder(null, null, LoggedUserData.orderItemsList,
                        "CartList", null, draftId)
                )
                cartViewModel.updateCartItem(draftId!!, draftOrder)
                val favDraftOrder = DraftOrderPost(
                    DraftOrder(null, null, LoggedUserData.favOrderDraft,
                        "WishList", null, wishListId)
                )
                favViewModel.updateFavtItem(wishListId!!,favDraftOrder)
                auth.signOut()
                LocalDataSource.getInstance().deleteCash(requireContext())
                Navigation.findNavController(requireView()).navigate(R.id.from_logout_to_home)
            }
        }


        personalBinding.settingsBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.from_personal_to_settings)
        }


    }

    private fun observeOnFavItems() {
        lifecycleScope.launch {
            favViewModel.favItems.collect {
                when (it) {
                    is ApiState.Loading -> {
                    /*    favouriteBinding.favLottieSplash.visibility = View.GONE
                        favouriteBinding.favLottieMessage.visibility = View.GONE
                        favouriteBinding.favprogressBar.visibility = View.VISIBLE*/
                    }
                    is ApiState.Success<*> -> {
                        if (it.date != null) {
                      //      favouriteBinding.favprogressBar.visibility = View.GONE
                            favDraftOrderPost = it.date as DraftOrderPost
                            if (LoggedUserData.favOrderDraft.size == 0) {
                                LoggedUserData.favOrderDraft =
                                    (favDraftOrderPost.draft_order.line_items
                                        ?: mutableListOf()) as MutableList<LineItem>
                            }
                            if(LoggedUserData.favOrderDraft.size ==1){
                                personalBinding.tvNoFav.visibility=View.VISIBLE

                            }else{
                                personalBinding.tvNoFav.visibility=View.GONE

                            }
                          //  personalBinding.tvNoFav.visibility=View.VISIBLE

                            wishListAdapter.updateWishList(LoggedUserData.favOrderDraft)
                        }
                    }
                    else -> {
                      //  favouriteBinding.favprogressBar.visibility = View.GONE
                        Snackbar.make(
                            requireView(),
                            "Failed to obtain data from api",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(true)
    }
    fun getOrders(){
        orderListViewModel.getOrders(userId)
        lifecycleScope.launch {
            orderListViewModel.accessOrderList.collect { result ->
                when (result) {
                    is ApiState.Success<*> -> {
                        // homeBinding.progressBar.visibility = View.GONE

                        var orders = result.date as List<Order>
                        personalBinding.personProgressBar.visibility= View.GONE
                        if (orders.size==0){
                            personalBinding.tvNoOrders.visibility=View.VISIBLE

                        }
                        else{
                            personalBinding.tvNoOrders.visibility=View.GONE
                        }


                        // smartCollections = brands?.smart_collections ?: listOf()
                        //  brandsAdapter.setBrandsList(smartCollections)
                       ordersAdapter.setOrderList(orders)
                    }
                    is ApiState.Failure -> {
                        //  homeBinding.progressBar.visibility = View.GONE

                    }
                    is ApiState.Loading -> {
                        //   homeBinding.progressBar.visibility = View.VISIBLE
                    }

                }

            }
        }
    }
    fun checkNetwork() {
        networkObservation = NetworkConectivityObserver(requireContext())
        lifecycleScope.launch {
            networkObservation.observeOnNetwork().collectLatest {
                when (it.name) {
                    "Avaliavle" -> {

                        Log.i("Internet", it.name)
                        retry()
                    }
                    "Lost" -> {
                        showInternetDialog()
                    }
                    InternetStatus.UnAvailable.name-> {
                        Log.i("Internet", it.name)
                        showInternetDialog()
                    }
                }
            }
        }
    }

    private fun showInternetDialog() {

        (context as MainActivity).showSnakeBar()
    }

    fun retry() {
        if(FirebaseAuth.getInstance().currentUser!=null){
        orderListViewModel.getOrders(userId)
        favViewModel.getFavItems(wishListId?:1592654688)
        }

    }


}