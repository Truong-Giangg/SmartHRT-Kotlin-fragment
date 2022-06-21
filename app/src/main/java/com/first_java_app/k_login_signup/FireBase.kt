package com.first_java_app.k_login_signup
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class FireBase {

    val database = Firebase.database

    var data: String = ""

    fun setFData(path: String, value: String) {
        val data = database.getReference(path).setValue(value)
    }
    fun getFData(path: String): String{

        // Read from the database
        database.getReference(path).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                data = value.toString()
                Log.d(TAG, "Value is: $value")
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        return data
    }

}