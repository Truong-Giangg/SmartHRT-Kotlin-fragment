package com.first_java_app.k_login_signup

import androidx.appcompat.app.AppCompatActivity
import com.first_java_app.k_login_signup.R
import android.widget.EditText
import com.first_java_app.k_login_signup.UserHelperClassGadget
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.first_java_app.k_login_signup.MainActivity
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.first_java_app.k_login_signup.MainMenu

class addWidget : AppCompatActivity(), View.OnClickListener {
    lateinit var addWidgetSwitch: Button
    lateinit var addWidgetSlider: Button
    lateinit var addWidgetTemp: Button
    var dButton = arrayOfNulls<Button>(9)
    var buttonIds =
        intArrayOf(R.id.D0, R.id.D1, R.id.D2, R.id.D3, R.id.D4, R.id.D5, R.id.D6, R.id.D7, R.id.D8)
    var widgetNameAdd: EditText? = null
    private var currentWidget = 0
    var widgetName_s = ""
    var espPin_s = ""
    var widgetType: String? = null
    lateinit var userGet: Array<UserHelperClassGadget?>
    var rootNode: FirebaseDatabase? = null
    var reference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addwidget)
        addWidgetSwitch = findViewById(R.id.addWidgetSwitch)
        addWidgetSlider = findViewById(R.id.addWidgetSlider)
        addWidgetTemp = findViewById(R.id.addWidgetTemp)
        widgetNameAdd = findViewById(R.id.widgetNameAdd)
        for (i in 0..8) {
            dButton[i] = findViewById(buttonIds[i])
        }
        //espPin = findViewById(R.id.espPin);
        addWidgetSlider.setOnClickListener(this@addWidget)
        addWidgetSwitch.setOnClickListener(this@addWidget)
        addWidgetTemp.setOnClickListener(this@addWidget)
        for (i in 0..8) {
            dButton[i]!!.setOnClickListener(this@addWidget)
            dButton[i]!!.setBackgroundColor(Color.GRAY)
        }
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode!!.reference.child("users").child(MainActivity.user_username_gadget!!)
            .child("user's gadget")
        reference!!.addListenerForSingleValueEvent(object : ValueEventListener {
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
    }

    override fun onClick(view: View) {
        if (view.id == R.id.addWidgetSwitch) {
            widgetName_s = widgetNameAdd!!.text.toString()
            widgetNameAdd!!.setText("") //clear the field when button pushed
            widgetType = "button"
            if (validInput()) gobackMainMenu(view)
        }
        if (view.id == R.id.addWidgetSlider) {
            widgetName_s = widgetNameAdd!!.text.toString()
            widgetNameAdd!!.setText("") //clear the field when button pushed
            widgetType = "seekbar"
            if (validInput()) gobackMainMenu(view)
        }
        if (view.id == R.id.addWidgetTemp) {
            widgetName_s = widgetNameAdd!!.text.toString()
            widgetNameAdd!!.setText("") //clear the field when button pushed
            widgetType = "temperature"
            if (validInput()) gobackMainMenu(view)
        }
        for (i in 0..8) {
            dButton[i]!!.setBackgroundColor(Color.GRAY)
            if (view.id == buttonIds[i]) {
                dButton[i]!!.setBackgroundColor(Color.GREEN)
                espPin_s = "D$i"
            }
        }
    }

    private fun validInput(): Boolean {
        for (i in 0 until currentWidget) {
            if (userGet[i]?.btnName.equals(widgetName_s)) {
                Toast.makeText(this@addWidget, "Tên đã tồn tại, chọn lại!!", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            if (userGet[i]!!.espPin == espPin_s) {
                Toast.makeText(
                    this@addWidget,
                    "Chân esp8266 đã chọn rồi, chọn lại!!",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        return if (widgetName_s.isEmpty()) {
            Toast.makeText(this@addWidget, "Tên không được bỏ trống!!", Toast.LENGTH_SHORT).show()
            false
        } else if (espPin_s.isEmpty()) {
            Toast.makeText(this@addWidget, "Hãy chọn 1 chân esp8266!!", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    fun gobackMainMenu(view: View?) {
        val helperClass = UserHelperClassGadget(
            currentWidget.toString(),
            widgetName_s,
            "0",
            widgetType,
            "null",
            espPin_s
        )
        reference!!.child(currentWidget.toString()).setValue(helperClass).addOnSuccessListener {
            // hide virtual keyboard
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

            //--------------push data to MainMenu acctivity via username------------
            val intent = Intent(this@addWidget, MainMenu::class.java)
            //intent.putExtra("username",MainActivity.user_username_gadget);
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            //--------------end push data to MainMenu acctivity via username------------
        }
    }
}