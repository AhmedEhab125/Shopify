package com.example.shopify.database

import android.content.Context
import android.content.SharedPreferences
import com.example.shopify.Models.FireBaseModel.MyFireBaseUser
import com.google.gson.Gson

class LocalDataSource {

lateinit private var sharedPreferences : SharedPreferences

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(): LocalDataSource {
            return INSTANCE ?: synchronized(this) {
               val instance = LocalDataSource()
                INSTANCE = instance
                instance
            }
        }

    }


  private  fun createUserInvoShared(context: Context) {
        sharedPreferences =context.getSharedPreferences("UserFireBase", Context.MODE_PRIVATE)
    }


    fun writeInShared(context: Context,loginUser: MyFireBaseUser) {
        sharedPreferences =context.getSharedPreferences("UserFireBase", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("UserFireBase", Gson().toJson(loginUser))
            apply()
        }

    }


    fun readFromShared(context: Context): MyFireBaseUser? {
        createUserInvoShared(context)
        return Gson().fromJson(sharedPreferences.getString("UserFireBase", ""), MyFireBaseUser::class.java)
    }


    fun deleteCash(context: Context){
        sharedPreferences =context.getSharedPreferences("UserFireBase", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("UserFireBase", null)
            apply()
        }
    }
}