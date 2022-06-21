package com.first_java_app.k_login_signup.Alarm

import android.app.Service
import android.media.MediaPlayer
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.first_java_app.k_login_signup.R
import com.first_java_app.k_login_signup.MainActivity
import android.widget.Toast

class Music : Service() {
    var mediaPlayer: MediaPlayer? = null
    private var id = 0
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e("toi tron music", "Xin chao")
        val nhankey = intent.extras!!.getString("extra")
        Log.e("Music Nhan Key", nhankey!!)
        if (nhankey == "on") {
            id = 1
        } else if (nhankey == "off") {
            id = 0
        }
        if (id == 1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.nokia_tune_original_ringtone_alarm)
            mediaPlayer!!.start()
            MainActivity.alarmActive = "1"

//            new CountDownTimer(2000, 100) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    Toast.makeText(Music.this, "Vui lòng tắt báo thức", Toast.LENGTH_SHORT).show();
//                }
//                @Override
//                public void onFinish() {
//
//                }
//            }.start();
            Toast.makeText(this@Music, "Vui lòng tắt báo thức", Toast.LENGTH_SHORT).show()
            id = 0
        } else if (id == 0) {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            MainActivity.alarmActive = "0"
        }
        return START_NOT_STICKY
    }
}