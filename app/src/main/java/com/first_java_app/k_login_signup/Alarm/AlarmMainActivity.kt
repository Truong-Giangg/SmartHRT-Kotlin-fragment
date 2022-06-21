package com.first_java_app.k_login_signup.Alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.first_java_app.k_login_signup.MainActivity
import com.first_java_app.k_login_signup.R
import com.first_java_app.k_login_signup.model.UserHelperClassGadget
import com.first_java_app.k_login_signup.removeWidget
import com.google.firebase.database.*
import java.util.*

class alarmMainActivity : AppCompatActivity() {
    //khai bao
    var btnHenGio: Button? = null
    var btnDungLai: Button? = null
    var txtHienThi: TextView? = null
    var timePicker: TimePicker? = null
    lateinit var calendar: Calendar
    var alarmManager: AlarmManager? = null
    var pendingIntent: PendingIntent? = null
    val delay = 100 //milliseconds
    lateinit var userGet: Array<UserHelperClassGadget?>
    private var currentWidget = 0
    val handler = Handler()
    var rootNode: FirebaseDatabase? = null
    var reference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        // anh xa
        btnHenGio = findViewById<View>(R.id.btnHenGio) as Button
        btnDungLai = findViewById<View>(R.id.btnDungLai) as Button
        txtHienThi = findViewById<View>(R.id.textViewAlarm) as TextView
        timePicker = findViewById<View>(R.id.timePicker) as TimePicker
        calendar = Calendar.getInstance() // thoi gian tren may
        alarmManager =
            getSystemService(ALARM_SERVICE) as AlarmManager // truy cap ALARM_SERVICE cua may  bao thuc
        val intent = Intent(
            this@alarmMainActivity,
            AlarmReceiver::class.java
        ) // truyen du lieu MainActivity -> Alarmrecever
        val intent1 = getIntent()
        if (intent1.getStringExtra("swgestureid") != null) {
            MainActivity.gestureChild = intent1.getStringExtra("swgestureid")
            Toast.makeText(
                this@alarmMainActivity,
                "child:" + MainActivity.gestureChild,
                Toast.LENGTH_LONG
            ).show()
        }
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode!!.reference.child("users").child(MainActivity.user_username_gadget!!)
            .child("user's gadget")
        reference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userNum = 0
                val size = dataSnapshot.childrenCount.toInt()
                userGet = arrayOfNulls<UserHelperClassGadget>(size)
                for (snapshot in dataSnapshot.children) {
                    userGet[userNum] =
                        snapshot.getValue(UserHelperClassGadget::class.java) //get data store to class
                    userNum++
                }
                currentWidget = dataSnapshot.childrenCount.toInt()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        handler.postDelayed(object : Runnable {
            override fun run() {
                for (i in 0 until currentWidget) {
                    if (userGet[i]?.btnID.equals(MainActivity.gestureChild)) {
                        if (MainActivity.alarmActive == "1") {
                            if (userGet[i]?.btnValue.equals("1")) {
                                userGet[i]?.btnValue = "0"
                                reference!!.child(MainActivity.gestureChild!!).setValue(userGet[i])
                                MainActivity.alarmActive = "0"
                            } else {
                                userGet[i]?.btnValue = "1"
                                reference!!.child(MainActivity.gestureChild!!).setValue(userGet[i])
                                MainActivity.alarmActive = "0"
                            }
                        }
                    }
                }
                handler.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())
        // khai bao su kien
        btnHenGio!!.setOnClickListener {
            calendar.set(Calendar.HOUR_OF_DAY, timePicker!!.currentHour) // lay gio
            calendar.set(Calendar.MINUTE, timePicker!!.currentMinute) // lay phut
            val gio = timePicker!!.currentHour
            val phut = timePicker!!.currentMinute

            // chuyen int sang chuoi
            var string_gio = gio.toString()
            var string_phut = phut.toString()
            if (gio > 12) {
                string_gio = (gio - 12).toString()
            }
            if (phut < 10) {
                string_phut = "0$phut"
            }
            intent.putExtra("extra", "on")
            pendingIntent = PendingIntent.getBroadcast(
                this@alarmMainActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
//            alarmManager!![AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent

            alarmManager!![AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()] = pendingIntent
            txtHienThi!!.text = "Giờ bạn đặt là $string_gio:$string_phut"

        }
        btnDungLai!!.setOnClickListener {
            txtHienThi!!.text = "Dừng lại"
            alarmManager!!.cancel(pendingIntent)
            intent.putExtra("extra", "off")
            sendBroadcast(intent)
        }
    }
}