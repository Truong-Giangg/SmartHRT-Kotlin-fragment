package com.first_java_app.k_login_signup.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(var fullName : String,var email: String, var password: String) : Parcelable {
    fun displayName() : String{
        return "Hi, ${fullName}"
    }
    fun isNull() : Boolean{
        return fullName.equals("") && email.equals("") && password.equals("");
    }
}