package com.first_java_app.k_login_signup.Alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("toi trong reveiver", "xin chao")
        val chuoi_string = intent.extras!!.getString("extra")
        Log.e("Ban truyen key", chuoi_string!!)
        val myIntent = Intent(context, Music::class.java)
        myIntent.putExtra("extra", chuoi_string)
        context.startService(myIntent)
    }
}