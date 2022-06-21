package com.first_java_app.k_login_signup


import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.first_java_app.k_login_signup.R
import com.first_java_app.k_login_signup.MainActivity
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.first_java_app.k_login_signup.chooseBtn
import com.first_java_app.k_login_signup.UserHelperClassGadget
import android.widget.TextView
import android.widget.LinearLayout
import com.google.firebase.database.DatabaseError
import android.content.Intent
import android.view.View

class chooseBtn : AppCompatActivity(), View.OnClickListener {
    var rootNode: FirebaseDatabase? = null
    var reference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_btn)
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode!!.reference.child("users").child(MainActivity.user_username_gadget!!)
            .child("user's gadget")
        reference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                size = dataSnapshot.childrenCount.toInt()
                swID = IntArray(size)
                var userNum = 0
                for (snapshot in dataSnapshot.children) {
                    val userget = snapshot.getValue(
                        UserHelperClassGadget::class.java
                    ) //get data store to class
                    if (userget!!.widType == "button") {
                        val gadgetList = TextView(this@chooseBtn)
                        gadgetList.setText(userget.btnName)
                        gadgetList.id = userget.btnID!!.toInt()
                        swID[userNum] = userget.btnID!!.toInt()
                        val ll = findViewById<View>(R.id.layoutswitch) as LinearLayout
                        val lp = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        lp.setMargins(10, 30, 10, 30) //for better layout
                        ll.addView(gadgetList, lp)
                        gadgetList.setOnClickListener(this@chooseBtn)
                        userNum++
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onClick(view: View) {
        for (i in 0 until size) {
            if (view.id == swID[i]) {
                //Toast.makeText(addGesture.this, "btn ID: "+swID[i], Toast.LENGTH_LONG).show();
                gotoGestureList(view, swID[i])
            }
        }
    }

    fun gotoGestureList(view: View?, swID: Int) {
        val intent = Intent(applicationContext, gestureList::class.java)
        MainActivity.gestureChild = swID.toString()
        intent.putExtra("swgestureid", MainActivity.gestureChild)
        startActivity(intent)
    }

    companion object {
        lateinit var swID: IntArray
        var size = 0
    }
}