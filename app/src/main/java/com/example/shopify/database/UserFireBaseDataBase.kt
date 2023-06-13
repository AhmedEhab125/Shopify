package com.example.shopify.database

import android.util.Log
import com.example.shopify.Models.FireBaseModel.MyFireBaseUser
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserFireBaseDataBase {



    companion object {
        fun insertUserInFireBase(user: MyFireBaseUser , firebaseUser : FirebaseUser){
            var dbRfrence = FirebaseDatabase.getInstance().getReference("Users")
            dbRfrence.child(firebaseUser.uid).setValue(user).addOnCompleteListener {
                Log.i("FireBaseDone","Added Successfuly")
            }.addOnFailureListener {
                Log.i("FireBaseErorr","There Is Error In FireBase")
            }
        }


       fun getUserFromFireBase(user : FirebaseUser)  {
       //     var myUser :MyFireBaseUser?= null
            var dbRfrence = FirebaseDatabase.getInstance().getReference().child("Users").child(user.uid)


            dbRfrence.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val myUser = snapshot.getValue(MyFireBaseUser::class.java)
                    if (myUser !=null) {
                        Log.i("MiladMizo", myUser.toString())
                        // write user in sharedPrefrence
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                  Log.i("Fetch Error","Sorry There Is An Error in Fire Base")

                }

            })

        }





    }



}