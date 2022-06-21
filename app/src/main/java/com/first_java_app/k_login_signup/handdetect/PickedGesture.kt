package com.first_java_app.k_login_signup.handdetect

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import android.widget.ScrollView
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.TextView
import android.view.Gravity
import android.graphics.Bitmap
import com.google.firebase.database.DatabaseError
import android.graphics.BitmapFactory
import android.content.res.AssetManager
import android.widget.ImageView
import com.first_java_app.k_login_signup.MainActivity
import com.first_java_app.k_login_signup.R
import java.io.IOException
import com.first_java_app.k_login_signup.model.UserHelperClassGadget

class pickedGesture : AppCompatActivity() {
    var rootNode: FirebaseDatabase? = null
    var reference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_gesture_picked)
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode!!.reference.child("users").child(MainActivity.user_username_gadget!!)
            .child("user's gadget")
        reference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                size = dataSnapshot.childrenCount.toInt()
                swID = IntArray(size)
                var userNum = 0
                val scrollView = ScrollView(this@pickedGesture)
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scrollView.layoutParams = layoutParams
                val linearLayout = LinearLayout(this@pickedGesture)
                val linearParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                linearLayout.orientation = LinearLayout.VERTICAL
                linearLayout.layoutParams = linearParams
                scrollView.addView(linearLayout)
                for (snapshot in dataSnapshot.children) {
                    val userget = snapshot.getValue(
                        UserHelperClassGadget::class.java
                    ) //get data store to class
                    if (userget!!.widType == "button") {
                        val gadgetList = TextView(this@pickedGesture)
                        gadgetList.setText(userget.btnName)
                        gadgetList.id = userget.btnID!!.toInt()
                        swID[userNum] = userget.btnID!!.toInt()
                        val params1 = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        params1.setMargins(0, 30, 0, 30)
                        params1.gravity = Gravity.CENTER
                        gadgetList.gravity = Gravity.CENTER
                        gadgetList.layoutParams = params1
                        linearLayout.addView(gadgetList)
                        for (i in 0..1) {
                            val img = ImageView(this@pickedGesture)
                            val bm = getBitmapFromAsset("gesture/" + userget.gestureT!![i] + ".jpg")
                            val params2 = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            params2.setMargins(0, 30, 0, 30)
                            params2.gravity = Gravity.CENTER
                            img.layoutParams = params2
                            linearLayout.addView(img)
                            img.setImageBitmap(bm)
                        }
                        userNum++
                    }
                }
                val linearLayout1 = findViewById<LinearLayout>(R.id.rootContainer)
                linearLayout1?.addView(scrollView)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getBitmapFromAsset(paramString: String): Bitmap? {
        val localObject: Any = resources.assets
        try {
            return BitmapFactory.decodeStream((localObject as AssetManager).open(paramString))
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return null
    } //    public void gotoGestureList(View view, int swID){

    //        Intent intent =new Intent(getApplicationContext(),gestureList.class);
    //        MainActivity.gestureChild = String.valueOf(swID);
    //        intent.putExtra("swgestureid", MainActivity.gestureChild);
    //        startActivity(intent);
    //    }
    companion object {
        lateinit var swID: IntArray
        var size = 0
    }
}