package com.first_java_app.k_login_signup.handdetect

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import android.widget.ScrollView
import android.widget.LinearLayout
import android.view.ViewGroup
import android.graphics.Bitmap
import android.view.Gravity
import android.content.Intent
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.widget.Toast
import android.graphics.BitmapFactory
import android.content.res.AssetManager
import android.view.View
import android.widget.ImageView
import com.first_java_app.k_login_signup.MainActivity
import com.first_java_app.k_login_signup.R
import java.io.IOException
import com.first_java_app.k_login_signup.model.UserHelperClassGadget
//https://tutorialwing.com/create-android-scrollview-programmatically-android/
class gestureList : AppCompatActivity(), View.OnClickListener {
    var onStatus = ""
    var rootNode: FirebaseDatabase? = null
    var reference: DatabaseReference? = null
    var count = 0
    private var currentWidget = 0
    lateinit var userGet: Array<UserHelperClassGadget?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesturelist)
        val scrollView = ScrollView(this@gestureList)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        scrollView.layoutParams = layoutParams
        val linearLayout = LinearLayout(this@gestureList)
        val linearParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = linearParams
        scrollView.addView(linearLayout)
        var gId = 0
        var c = 'A'
        while (c <= 'Y') {
            val img = ImageView(this@gestureList)
            val bm = getBitmapFromAsset("gesture/$c.jpg")
            val params1 = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params1.setMargins(0, 30, 0, 30)
            params1.gravity = Gravity.CENTER
            img.layoutParams = params1
            linearLayout.addView(img)
            img.setImageBitmap(bm)
            img.id = gId
            gestureID[gId] = gId
            gestureType[gId] = c.toString()
            img.setOnClickListener(this@gestureList)
            gId++
            ++c
        }
        val linearLayout1 = findViewById<LinearLayout>(R.id.rootContainer)
        linearLayout1?.addView(scrollView)
        val intent = intent
        if (intent.getStringExtra("swgestureid") != null) {
            MainActivity.gestureChild = intent.getStringExtra("swgestureid")
            //Toast.makeText(gestureList.this, "child:"+MainActivity.gestureChild, Toast.LENGTH_LONG).show();
        }
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode!!.reference.child("users").child(MainActivity.user_username_gadget!!)
            .child("user's gadget")
        reference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            //get data from firebase only once
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userNum = 0
                val size = dataSnapshot.childrenCount.toInt()
                userGet = arrayOfNulls(size)
                for (snapshot in dataSnapshot.children) {
                    userGet[userNum] = snapshot.getValue(
                        UserHelperClassGadget::class.java
                    ) //get data store to class
                    userNum++
                }
                currentWidget = dataSnapshot.childrenCount.toInt()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        Toast.makeText(this@gestureList, "Chọn hành động bật", Toast.LENGTH_LONG).show()
    }

    override fun onClick(view: View) {
        for (i in 0..24) {
            if (view.id == gestureID[i]) {
                count++
                onStatus = onStatus + gestureType[i]
                if (count == 1) {
                    Toast.makeText(this@gestureList, "Chọn hành động tắt", Toast.LENGTH_SHORT)
                        .show()
                }
                if (count == 2) {
                    count = 0
                    if (validPick()) {
                        userGet[MainActivity.gestureChild!!.toInt()]!!.gestureT = onStatus
                        reference!!.child(MainActivity.gestureChild.toString())
                            .setValue(userGet[MainActivity.gestureChild!!.toInt()])
                        gotoCameraA(view)
                    }
                    onStatus = ""
                }
            }
        }
    }

    private fun validPick(): Boolean {
        for (i in 0 until currentWidget) {
            if (userGet[i]!!.widType == "button") {
                if (userGet[i]!!.gestureT!![0] == onStatus[0] || userGet[i]!!.gestureT!![0] == onStatus[1]) {
                    Toast.makeText(
                        this@gestureList,
                        "Cử chỉ bật đã được chọn rồi, mời chọn lại!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                if (userGet[i]!!.gestureT!![1] == onStatus[1] || userGet[i]!!.gestureT!![1] == onStatus[0]) {
                    Toast.makeText(
                        this@gestureList,
                        "Cử chỉ tắt đã được chọn rồi, mời chọn lại!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                if (onStatus[0] == onStatus[1]) {
                    Toast.makeText(
                        this@gestureList,
                        "Cử chỉ bật bị trùng với cử chỉ tắt, chọn lại!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
        }
        return true
    }

    private fun getBitmapFromAsset(paramString: String): Bitmap? {
        val localObject: Any = resources.assets
        try {
            return BitmapFactory.decodeStream((localObject as AssetManager).open(paramString))
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return null
    }

    fun gotoCameraA(view: View?) {
        val intent = Intent(applicationContext, CameraActivity::class.java)
        startActivity(intent)
    }

    companion object {
        var gestureID = IntArray(25)
        var gestureType = arrayOfNulls<String>(25)
    }
}