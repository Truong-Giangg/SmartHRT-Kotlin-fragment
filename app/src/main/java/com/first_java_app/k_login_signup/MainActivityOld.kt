package com.first_java_app.k_login_signup
//
//import androidx.appcompat.app.AppCompatActivity
//import android.view.animation.Animation
//import android.widget.TextView
//import android.os.Bundle
//import android.view.WindowManager
//import com.first_java_app.k_login_signup.R
//import android.content.Intent
//import android.os.Handler
//import android.view.animation.AnimationUtils
//import android.widget.ImageView
//import com.first_java_app.k_login_signup.MainActivityOld
//
//class MainActivityOld : AppCompatActivity() {
//    var topAnim: Animation? = null
//    var bottomAnim: Animation? = null
//    var image: ImageView? = null
//    var logo: TextView? = null
//    var slogan: TextView? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        ) //full screen
//        setContentView(R.layout.activity_main)
//        //anima
//        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
//        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
//        //gan
//        image = findViewById(R.id.imageView)
//        logo = findViewById(R.id.textView2) // gan
//        slogan = findViewById(R.id.textView3)
//        image.setAnimation(topAnim)
//        logo.setAnimation(bottomAnim) //add animation to image
//        slogan.setAnimation(bottomAnim)
//        Handler().postDelayed({
//            val intent = Intent(this@MainActivity, Login::class.java)
//            startActivity(intent)
//            finish()
//        }, SPLASH_SCREEN.toLong())
//    }
//
//    companion object {
//        private const val SPLASH_SCREEN = 5000
//        var user_username_gadget: String? = null
//        var gestureChild: String? = null
//        var alarmActive = "0" //variable for handGesture class
//    }
//}