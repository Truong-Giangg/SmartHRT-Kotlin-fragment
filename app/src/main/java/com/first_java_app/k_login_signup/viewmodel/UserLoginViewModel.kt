package com.first_java_app.k_login_signup.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.first_java_app.k_login_signup.model.User
import java.util.regex.Pattern

enum class Error {
    ERROR_EMAIL,
    ERROR_PASSWORD,
}

class Resp(val isSuccess: Boolean, val error: Error?)

class UserLoginViewModel : ViewModel() {
    var user =User("","","")
    private var _isSuccessEvent: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccessEvent: LiveData<Boolean>
        get() = _isSuccessEvent

    private var _isErrorEvent: MutableLiveData<String> = MutableLiveData()
    val isErrorEvent: LiveData<String>
        get() = _isErrorEvent

    fun checkEmailAndPassword(email: String, password: String, username: String) {
        //kiem tra format email
        val isValidEmail = isEmailValid(email)
        if (!isValidEmail) {
            _isErrorEvent.postValue("email không hợp lệ")
            return
        }
        //password length > 8 && < 10
        val isValidPassword = isPasswordValid(password)
        if (!isValidPassword) {
            _isErrorEvent.postValue("Mật khẩu phải có ít nhấ 8 ký tự (bao gồm viết hoa, viết thường, ký tự đặc biệt)")
            return
        }
        val isValidPUser = isUserValid(username)
        if (isValidPUser) {
            _isErrorEvent.postValue("Username không được bỏ trống")
            return
        }
        _isSuccessEvent.postValue(true)
    }
    // kiem tra nguoi dung nhap format email
    private fun isUserValid(username: String): Boolean {
        return username.isEmpty()
    }
    // kiem tra nguoi dung nhap format email
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    // kiem tra nguoi dung nhap nformat password
    private fun isPasswordValid(password: String): Boolean {
        val checkpass= Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()])(?=\\S+$).{8,}$")
        return checkpass.matcher(password).matches()
    }
}