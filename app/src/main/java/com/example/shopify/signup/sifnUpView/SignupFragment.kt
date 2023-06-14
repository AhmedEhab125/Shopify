package com.example.shopify.signup.sifnUpView

import android.location.Address
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.shopify.Models.FireBaseModel.MyFireBaseUser
import com.example.shopify.Models.draftOrderCreation.DraftOrder
import com.example.shopify.Models.draftOrderCreation.DraftOrderPost
import com.example.shopify.Models.draftOrderCreation.LineItem
import com.example.shopify.Models.productDetails.ProductModel
import com.example.shopify.Models.registrashonModel.Addresse
import com.example.shopify.Models.registrashonModel.Customer
import com.example.shopify.Models.registrashonModel.CustomerRegistrationModel
import com.example.shopify.R
import com.example.shopify.database.UserFireBaseDataBase
import com.example.shopify.databinding.FragmentSignupBinding
import com.example.shopify.login.LoginFragmentDirections
import com.example.shopify.mainActivity.MainActivity
import com.example.shopify.nework.ApiState
import com.example.shopify.nework.ShopifyAPi
import com.example.shopify.repo.RemoteSource
import com.example.shopify.signup.model.ConcreteRegisterUser
import com.example.shopify.signup.signUpViewModel.SignUpViewModel
import com.example.shopify.signup.signUpViewModel.SignUpViewModelFactory
import com.example.shopify.utiltes.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {
    lateinit var binding: FragmentSignupBinding
    lateinit var auth: FirebaseAuth
    lateinit var password: String
    lateinit var email: String
    lateinit var firstName: String
    lateinit var secondName: String
    lateinit var address: String
    lateinit var city: String
    lateinit var country: String
    lateinit var phone: String
    lateinit var signupViewModel: SignUpViewModel
    lateinit var signupFactory: SignUpViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signupFactory =
            SignUpViewModelFactory(ConcreteRegisterUser(RemoteSource(ShopifyAPi.retrofitService)))
        signupViewModel =
            ViewModelProvider(requireActivity(), signupFactory).get(SignUpViewModel::class.java)


        binding.signUpBtn.setOnClickListener {
            email = binding.signEmailTF.text.toString()
            password = binding.signPassTF.text.toString()
            firstName = binding.signFirstNameTF.text.toString()
            secondName = binding.signLastNameTF.text.toString()
            phone = binding.signPhoneTF.text.toString()
            address = binding.signStreetTF.text.toString()
            city = binding.signCityTF.text.toString()
            country = binding.signCounterTF.text.toString()
            if (checkAllFalid()) {
                binding.signUpPrograssBar.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        auth.signOut()
                        Toast.makeText(requireContext(), "Sign In Sussessfuly", Toast.LENGTH_SHORT)
                            .show()
                        //create User Object And Post It To API
                        val user = it.result.user
                        creatUser(user as FirebaseUser)
                        Log.i(
                            "user",
                            firstName + "" + secondName + address + "" + city + "" + country + "" + phone + "" + email
                        )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            it.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.i("erorr", it.exception.toString())
                    }

                }
            }
        }

    }


    override fun onResume() {
        super.onResume()
        (context as MainActivity).hideNavigationBar(false)
    }


    private fun checkAllFalid(): Boolean {
        if (binding.signFirstNameTF.text.toString() == "") {
            binding.signFirstNameTFLayout.error = "This Is Required Filed"
            return false
        }
        if (binding.signLastNameTF.text.toString() == "") {
            binding.signLastNameTFLayout.error = "This Is Required Filed"
            return false
        }
        val email = binding.signEmailTF.text
        if (binding.signEmailTF.text.toString() == "") {
            binding.signEmailTFLayout.error = "This Is Required Filed"
            return false
        }
        /*    if(!email?.matches(Constants.emailRegex.toRegex())!!){
                binding.signEmailTFLayout.error = "Check Email Format"
                return false
            }*/
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.signEmailTFLayout.error = "Check Email Format"
            return false
        }
        if (binding.signPassTF.text.toString() == "") {
            binding.signPassTFLayout.error = "This Is Required Filed"
            binding.signPassTFLayout.errorIconDrawable = null
            return false
        }
        if (binding.signPassTF.length() < 8) {
            binding.signPassTFLayout.error = "Password Should At Least 8 Character Or Numbers"
            binding.signPassTFLayout.errorIconDrawable = null
            return false
        }
        if (binding.signCongrmPassTF.text.toString() == "") {
            binding.signCongrmPassTFLayout.error = "This Is Required Filed"
            binding.signPassTFLayout.errorIconDrawable = null
            return false
        }
        if (binding.signPassTF.text.toString() != binding.signCongrmPassTF.text.toString()) {
            binding.signCongrmPassTFLayout.error = "Passwords Not Matches"
            return false
        }

        if (binding.signStreetTF.text.toString() == "") {
            binding.signStreetTFLayout.error = "This Is Required Filed"
            return false
        }
        if (binding.signCityTF.text.toString() == "") {
            binding.signCityTFLayout.error = "This Is Required Filed"
            return false
        }
        if (binding.signCounterTF.text.toString() == "") {
            binding.signCountryTFLayout.error = "This Is Required Filed"
            return false
        }
        if (binding.signPhoneTF.text.toString() == "" || binding.signPhoneTF.length() != 11) {
            binding.signPhoneTFLayout.error = "Please Enter Valid Phone Numder"
            return false
        }

        return true
    }


    private fun creatUser(fireBaseUser: FirebaseUser) {
        val address = listOf<Addresse>(
            Addresse(
                address1 = address,
                city,
                country,
                null,
                null,
                null,
                null,
                null
            )
        )

        val customer = Customer(
            null,
            addresses = address,
            email,
            firstName,
            secondName,
            password,
            password,
            phone,
            null,
            true
        )

        val user = CustomerRegistrationModel(customer)
        signupViewModel.registerUserToApi(user)


        var draftOrder = DraftOrder(
            null,
            null,
            listOf(
                LineItem(
                    null,
                    null,
                    "Custome Item",
                    "20.0",
                    null,
                    null,
                    1,
                    null,
                    "Custom Item",
                    null,
                    null,
                    null
                )
            ),
            "WishList",
            true,
            null
        )
        var draftOrderPost = DraftOrderPost(draftOrder)


        var draftOrder2 = DraftOrder(
            null,
            null,
            listOf(
                LineItem(
                    null,
                    null,
                    "Custome Item",
                    "20.0",
                    null,
                    null,
                    1,
                    null,
                    "Custom Item",
                    null,
                    null,
                    null
                )
            ),
            "CartList",
            true,
            null
        )
        var draftOrderPost2 = DraftOrderPost(draftOrder2)


        var myFirebaseUser: MyFireBaseUser? = MyFireBaseUser()


        lifecycleScope.launch {
            signupViewModel.userInfo.collect {
                when (it) {
                    is ApiState.Loading -> {
                        Log.i("Loading", "It's Loading")
                        //  binding.progressBar5.visibility = View.VISIBLE
                    }
                    is ApiState.Success<*> -> {
                        val myUser = it.date as? CustomerRegistrationModel
                        Log.i("Mizooo", myUser?.customer?.id.toString())
                        myFirebaseUser?.userId = myUser?.customer?.id
                        myFirebaseUser?.firsName = myUser?.customer?.first_name
                        signupViewModel.postWishListDraftPrder(draftOrderPost)
                    }
                    else -> {
                        val snakbar = Snackbar.make(
                            requireView(),
                            "There Is Failureee",
                            Snackbar.LENGTH_LONG
                        ).setAction("Action", null)
                        snakbar.show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            signupViewModel.wishListDraftOrder.collect {
                when (it) {
                    is ApiState.Loading -> {
                        //    Log.i("Loading","It's Loading")
                        //  binding.progressBar5.visibility = View.VISIBLE
                    }
                    is ApiState.Success<*> -> {
                        val myDraftOrder = it.date as DraftOrderPost
                        Log.i(
                            "Hadiaaaa",
                            myDraftOrder.draft_order.id.toString() + "" + myDraftOrder.draft_order.note
                        )
                        myFirebaseUser?.whiDraftOedredId = myDraftOrder.draft_order.id
                        signupViewModel.postCartDraftOrder(draftOrderPost2)
                    }
                    else -> {
                        val snakbar = Snackbar.make(
                            requireView(),
                            "There Is Failureee on draft order",
                            Snackbar.LENGTH_LONG
                        ).setAction("Action", null)
                        snakbar.show()
                        Log.i("Post Failuer", "Erorrrrrrrr")
                    }
                }
            }
        }

        lifecycleScope.launch {
            signupViewModel.cartListDraftOrder.collect {
                when (it) {
                    is ApiState.Loading -> {
                        //    Log.i("Loading","It's Loading")
                        //  binding.progressBar5.visibility = View.VISIBLE
                    }
                    is ApiState.Success<*> -> {
                        val myDraftOrder = it.date as DraftOrderPost
                        Log.i(
                            "Esaaaaam",
                            myDraftOrder.draft_order.id.toString() + "" + myDraftOrder.draft_order.note
                        )
                        myFirebaseUser?.cartdraftOrderId = myDraftOrder.draft_order.id
                        UserFireBaseDataBase.insertUserInFireBase(myFirebaseUser!!, fireBaseUser)
                        binding.signUpPrograssBar.visibility = View.GONE
                        val from = requireArguments().getString("from")!!
                        val id = requireArguments().getLong("id")
                        val action =
                            SignupFragmentDirections.actionSignupFragmentToLoginFragment(from, id)
                        Navigation.findNavController(requireView()).navigate(action)

                    }
                    else -> {
                        val snakbar = Snackbar.make(
                            requireView(),
                            "There Is Failureee on draft order",
                            Snackbar.LENGTH_LONG
                        ).setAction("Action", null)
                        snakbar.show()
                        Log.i("Post Failuer", "Erorrrrrrrr")
                    }
                }
            }
        }
    }


}