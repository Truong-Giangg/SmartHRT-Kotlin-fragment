package com.first_java_app.k_login_signup

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.first_java_app.k_login_signup.model.UserHelperClassGadget
class removeWidget : AppCompatActivity(), View.OnClickListener {
    lateinit var rmWidget: Button
    var widgetNameRm: EditText? = null
    var swap = false
    private var currentWidget = 0
    var widgetName_s: String? = null
    lateinit var userGet: Array<UserHelperClassGadget?>
    var rootNode: FirebaseDatabase? = null
    var reference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_removewidget)
        rmWidget = findViewById(R.id.removeWidget)
        widgetNameRm = findViewById(R.id.widgetNamerm)
        rmWidget.setOnClickListener(this@removeWidget)
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
                //Toast.makeText(removeWidget.this, "currentWidget: "+currentWidget, Toast.LENGTH_LONG).show();
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onClick(view: View) {
        if (view.id == R.id.removeWidget) {   // remove button
            widgetName_s = widgetNameRm!!.text.toString()
            for (i in 0 until currentWidget) {
                if (widgetName_s == userGet[i]?.btnName) { //delete child based on its name
                    //reference.child(userGet[i].getBtnID()).removeValue();
                    swap = true
                }
                if (swap) { // create another child and move next data to it
                    if (i != currentWidget - 1) {
                        val movedChild = UserHelperClassGadget(
                            userGet[i]!!.btnID,
                            userGet[i + 1]?.btnName,
                            userGet[i + 1]?.btnValue,
                            userGet[i + 1]!!.widType,
                            userGet[i + 1]!!.gestureT,
                            userGet[i + 1]!!.espPin
                        )
                        reference!!.child(userGet[i]!!.btnID.toString()).setValue(movedChild)
                    } else {
                        reference!!.child(userGet[currentWidget - 1]!!.btnID.toString())
                            .removeValue().addOnSuccessListener { gobackMainMenu(view) }
                    }
                }
            }
        }
    }

    fun gobackMainMenu(view: View?) {
        // hide virtual keyboard
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        val intent = Intent(this@removeWidget, MainMenu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}