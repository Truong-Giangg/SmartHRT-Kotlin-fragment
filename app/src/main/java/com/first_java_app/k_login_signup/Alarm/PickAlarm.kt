package com.first_java_app.k_login_signup.Alarm

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.first_java_app.k_login_signup.R
import com.first_java_app.k_login_signup.MainActivity
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import android.widget.TextView
import android.widget.LinearLayout
import com.google.firebase.database.DatabaseError
import android.widget.Toast
import android.content.Intent
import android.view.View
import com.first_java_app.k_login_signup.model.UserHelperClassGadget

class pickAlarm : AppCompatActivity(), View.OnClickListener {
    var rootNode: FirebaseDatabase? = null
    var reference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickalarm)
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode!!.reference.child("users").child(MainActivity.user_username_gadget!!)
            .child("user's gadget")
        reference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                size = dataSnapshot.childrenCount.toInt()
                swID = IntArray(size)
                var userNum = 0
                for (snapshot in dataSnapshot.children) {
                    val userget: UserHelperClassGadget? =
                        snapshot.getValue(UserHelperClassGadget::class.java) //get data store to class
                    if (userget != null) {
                        if (userget.widType.equals("button")) {
                            val gadgetList = TextView(this@pickAlarm)
                            gadgetList.setText(userget?.btnName)
                            if (userget != null) {
                                gadgetList.id = userget.btnID?.toInt()!!
                            }
                            if (userget != null) {
                                swID[userNum] = userget.btnID?.toInt()!!
                            }
                            val ll = findViewById<View>(R.id.layoutswitch) as LinearLayout
                            val lp = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            lp.setMargins(10, 30, 10, 30) //for better layout
                            ll.addView(gadgetList, lp)
                            gadgetList.setOnClickListener(this@pickAlarm)
                            userNum++
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onClick(view: View) {
        for (i in 0 until size) {
            if (view.id == swID[i]) {
                Toast.makeText(this@pickAlarm, "btn ID: " + swID[i], Toast.LENGTH_LONG).show()
                gotoalarmMainactivity(view, swID[i])
            }
        }
    }

    fun gotoalarmMainactivity(view: View?, swID: Int) {
        val intent = Intent(applicationContext, alarmMainActivity::class.java)
        MainActivity.gestureChild = swID.toString()
        intent.putExtra("swgestureid", MainActivity.gestureChild)
        startActivity(intent)
    }

    companion object {
        lateinit var swID: IntArray
        var size = 0
    }
}