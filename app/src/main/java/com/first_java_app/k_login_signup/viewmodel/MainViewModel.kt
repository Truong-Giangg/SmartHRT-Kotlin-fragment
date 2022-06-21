package com.first_java_app.k_login_signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class Error{
    ERROR_EMAIL,
    ERROR_PASSWORD
}
class Resp(val isSuccess:Boolean,val error: Error?)

class MainViewModel : ViewModel() {
    // cho phep thay doi du lieu trong viewModel ,Ngoai viewmodel ra chi duoc doc du lieu
    private var _isSuccessEvent: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccessEvent:LiveData<Boolean>
         get() = _isSuccessEvent

    private var _isErrorEvent: MutableLiveData<String> = MutableLiveData() // Tra ve noi dung error
    val isErrorEvent:LiveData<String>
        get() = _isErrorEvent

    fun checkEmailAndPassword(email: String,password: String){
        // Kiểm tra email
        val isValidEmail = isEmailValid(email)
        if(!isValidEmail){
            _isErrorEvent.postValue("Email khong hop le")
            return
        }
        // Kiểm tra password
        val isValidPassword = isPasswordValid(password)
        if(!isValidPassword){
            _isErrorEvent.postValue("Password khong hop le")
            return
        }
        _isSuccessEvent.postValue(true)
    }
    private fun isPasswordValid(password: String): Boolean {
        // return password.length > 8 && password.length <10
        return password.length in 8..10
    }
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
