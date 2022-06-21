package com.first_java_app.k_login_signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    companion object {
        private const val SPLASH_SCREEN = 5000
        var user_username_gadget: String? = "giang"
        var gestureChild: String? = null
        var alarmActive = "0" //variable for handGesture class
    }
}