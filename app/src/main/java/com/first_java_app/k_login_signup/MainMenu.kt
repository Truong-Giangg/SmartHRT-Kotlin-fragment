package com.first_java_app.k_login_signup

import androidx.appcompat.app.AppCompatActivity
import android.widget.SeekBar.OnSeekBarChangeListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.speech.RecognizerIntent
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.first_java_app.k_login_signup.Alarm.pickAlarm
import com.first_java_app.k_login_signup.handdetect.CameraActivity
import com.first_java_app.k_login_signup.ipCam.ipCamere
import java.lang.Exception
import com.first_java_app.k_login_signup.model.UserHelperClassGadget

class MainMenu : AppCompatActivity(), View.OnClickListener, OnSeekBarChangeListener,
    CompoundButton.OnCheckedChangeListener {
    var userTop: TextView? = null
    var layout: LinearLayout? = null
    lateinit var txViewName: Array<String?>
    private var currentWidget = 0
    lateinit var userGet: Array<UserHelperClassGadget?>
    var rootNode: FirebaseDatabase? = null
    var reference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        layout = findViewById(R.id.layout)
        userTop = findViewById(R.id.userName)
        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_alarm -> startActivity(Intent(this@MainMenu, pickAlarm::class.java))
                R.id.action_voice -> {
                    //startActivity(new Intent(MainMenu.this,Voice.class));
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    startActivityForResult(intent, 100)
                }
                R.id.action_gesture -> startActivity(
                    Intent(
                        this@MainMenu,
                        CameraActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                R.id.action_ipcam -> startActivity(Intent(this@MainMenu, ipCamere::class.java))
//                R.id.action_logout -> {
//                    startActivity(Intent(this@MainMenu, Login::class.java))
//                    finish()
//                }
            }
            true
        }
        //--------------fetch data from previous activity----------
        val intent = intent
        if (intent.getStringExtra("username") != null) {
            MainActivity.user_username_gadget = intent.getStringExtra("username")
        }
        //--------------end fetch data from previous activity----------
        rootNode = FirebaseDatabase.getInstance()
        reference = MainActivity.user_username_gadget?.let {
            rootNode!!.reference.child("users").child(it)
                .child("user's gadget")
        }
        reference!!.addValueEventListener(object : ValueEventListener {
            // get data from firebase when change
            //        reference.addListenerForSingleValueEvent(new ValueEventListener() { //get data from firebase only once
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userNum = 0
                val size = dataSnapshot.childrenCount.toInt()
                txViewName = arrayOfNulls(size)
                addedWidgetSW = arrayOfNulls(size)
                addedWidgetSB = arrayOfNulls(size)
                addedWidgetTX = arrayOfNulls(size)
                userGet = arrayOfNulls<UserHelperClassGadget>(size)
                clearLayout()
//                userTop.setText(MainActivity.user_username_gadget)
                for (snapshot in dataSnapshot.children) {
                    userGet[userNum] =
                        snapshot.getValue(UserHelperClassGadget::class.java) //get data store to class
                    if (userGet[userNum]?.widType.equals("button")) {
                        userGet[userNum]?.btnID?.let {
                            createSwitch(
                                addedWidgetSW,
                                userNum,
                                userGet[userNum]?.widType,
                                it,
                                userGet[userNum]?.btnName,
                                userGet[userNum]?.btnValue
                            )
                        }
                    }
                    if (userGet[userNum]?.widType.equals("seekbar")) {
                        userGet[userNum]?.btnID?.let {
                            userGet[userNum]?.btnValue?.let { it1 ->
                                createSeekBar(
                                    addedWidgetSB,
                                    userNum,
                                    userGet[userNum]?.widType,
                                    it,
                                    userGet[userNum]?.btnName,
                                    it1
                                )
                            }
                        }
                    }
                    if (userGet[userNum]?.widType.equals("temperature")) {
                        userGet[userNum]?.btnID?.let {
                            userGet[userNum]?.btnValue?.let { it1 ->
                                createTemp(
                                    addedWidgetTX,
                                    userNum,
                                    userGet[userNum]?.widType,
                                    it,
                                    userGet[userNum]?.btnName,
                                    it1
                                )
                            }
                        }
                    }
                    userNum++
                }
                currentWidget = dataSnapshot.childrenCount.toInt()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {}
    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {
        for (i in 0 until currentWidget) {
            if (seekBar.id == userGet[i]?.btnID?.toInt()) {
                if (userGet[i]?.widType.equals("seekbar")) {
                    userGet[i]?.btnValue = seekBar.progress.toString()
                    pushSbData2Firebase(
                        addedWidgetSB, i, addedWidgetSB[i]!!
                            .id.toString()
                    )
                }
            }
        }
    }

    override fun onClick(view: View) {}
    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        for (i in 0 until currentWidget) {
            if (buttonView.isPressed) {
                if (buttonView.id == userGet[i]?.btnID?.toInt()) {
                    if (userGet[i]?.widType.equals("button")) {
                        pushSwData2Firebase(
                            addedWidgetSW, i, addedWidgetSW[i]!!
                                .id.toString()
                        )
                    }
                }
            }
            //System.out.println("type:"+userGet[i].getWidType());
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            val a: String = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)!![0]
            for (i in 0 until currentWidget) {
                if (userGet[i]?.widType.equals("button")) {
                    if (a.contentEquals("Bật " + userGet[i]?.btnName) || a.contentEquals("bật " + userGet[i]?.btnName)) {
                        userGet[i]?.btnValue = "1"
                        userGet[i]?.btnID?.let { reference!!.child(it).setValue(userGet[i]) }
                        Toast.makeText(
                            this@MainMenu,
                            "đã bật " + userGet[i]?.btnName,
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (a.contentEquals("Tắt " + userGet[i]?.btnName) || a.contentEquals(
                            "tắt " + userGet[i]?.btnName
                        )
                    ) {
                        userGet[i]?.btnValue = "0"
                        userGet[i]?.btnID?.let { reference!!.child(it).setValue(userGet[i]) }
                        Toast.makeText(
                            this@MainMenu,
                            "đã tắt " + userGet[i]?.btnName,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    //                else {
//                    Toast.makeText(Voice.this, "không tìm thấy thiết bị! ", Toast.LENGTH_LONG).show();
//                }
                }
            }
        } catch (e: Exception) {
            val intent = intent
            finish()
            startActivity(intent)
        }
    }

    fun createSwitch(
        sw: Array<Switch?>,
        count: Int,
        swType: String?,
        swId: String,
        swName: String?,
        swValue: String?
    ) {
        addedWidgetSW[count] = Switch(this@MainMenu)
        addedWidgetSW[count]!!.text = swName
        addedWidgetSW[count]!!.id = swId.toInt()
        createLayoutForSwitches(sw, count)
        addedWidgetSW[count] = findViewById(swId.toInt())
        addedWidgetSW[count]?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            onCheckedChanged(
                buttonView,
                isChecked
            )
        }) // calling onClick() method for new button
        if (userGet[count]?.btnValue.equals("1")) {
            addedWidgetSW[count]?.setChecked(true) //set the current state of a Switch
        } else {
            addedWidgetSW[count]?.setChecked(false)
        }
    }

    fun createSeekBar(
        sb: Array<SeekBar?>,
        count: Int,
        sbType: String?,
        sbId: String,
        sbName: String?,
        sbValue: String
    ) {
        addedWidgetSB[count] = SeekBar(this@MainMenu)
        addedWidgetSB[count]!!.id = sbId.toInt()
        createLayoutForSeekbar(sb, count)
        addedWidgetSB[count] = findViewById(sbId.toInt())
        addedWidgetSB[count]?.setMax(256)
        addedWidgetSB[count]?.setOnSeekBarChangeListener(this@MainMenu)
        addedWidgetSB[count]?.setProgress(sbValue.toInt())
    }

    fun createTemp(
        tx: Array<TextView?>,
        count: Int,
        txType: String?,
        txId: String,
        txName: String?,
        txValue: String
    ) {
        addedWidgetTX[count] = TextView(this@MainMenu)
        addedWidgetTX[count]!!.id = txId.toInt()
        txViewName[count] = txName
        createLayoutForTemp(tx, count)
        addedWidgetTX[count]!!.gravity = Gravity.CENTER
        addedWidgetTX[count]!!.text = "$txValue celsius"
    }

    fun createLayoutForSwitches(sw: Array<Switch?>, count: Int) {
        val ll = findViewById<View>(R.id.layoutswitch) as LinearLayout
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lp.gravity = Gravity.CENTER
        sw[count]!!.gravity = Gravity.CENTER
        lp.setMargins(10, 30, 10, 30) //for better layout
        if (sw[count]!!.parent != null) {
            (sw[count]!!.parent as ViewGroup).removeView(sw[count]) // <- fix
        }
        ll.addView(sw[count], lp)
    }

    fun createLayoutForSeekbar(sb: Array<SeekBar?>, count: Int) {
        val ll = findViewById<View>(R.id.layoutseekbar) as LinearLayout
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lp.setMargins(10, 30, 10, 30) //for better layout
        lp.gravity = Gravity.CENTER
        if (sb[count]!!.parent != null) {
            (sb[count]!!.parent as ViewGroup).removeView(sb[count]) // <- fix
        }
        val textView = TextView(this@MainMenu)
        textView.setText(userGet[count]?.btnName)
        textView.gravity = Gravity.CENTER
        ll.addView(textView)
        ll.addView(sb[count], lp)
    }

    fun createLayoutForTemp(tx: Array<TextView?>, count: Int) {
        val ll = findViewById<View>(R.id.layoutTemp) as LinearLayout
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lp.setMargins(10, 30, 10, 30) //for better layout
        lp.gravity = Gravity.CENTER
        if (tx[count]!!.parent != null) {
            (tx[count]!!.parent as ViewGroup).removeView(tx[count]) // <- fix
        }
        val textView = TextView(this@MainMenu)
        textView.text = txViewName[count]
        textView.gravity = Gravity.CENTER
        ll.addView(textView)
        ll.addView(tx[count], lp)
    }

    private fun clearLayout() {
        val ll = findViewById<View>(R.id.layoutTemp) as LinearLayout
        ll.removeAllViews()
        val ll1 = findViewById<View>(R.id.layoutswitch) as LinearLayout
        ll1.removeAllViews()
        val ll2 = findViewById<View>(R.id.layoutseekbar) as LinearLayout
        ll2.removeAllViews()
    }

    fun pushSwData2Firebase(sw: Array<Switch?>?, count: Int, firebaseChild: String?) {
        if (addedWidgetSW[count]!!.isChecked) {
            userGet[count]?.btnValue = "1"
            reference!!.child(firebaseChild!!).setValue(userGet[count])
        } else {
            userGet[count]?.btnValue = "0"
            reference!!.child(firebaseChild!!).setValue(userGet[count])
        }
    }

    fun pushSbData2Firebase(sb: Array<SeekBar?>?, count: Int, firebaseChild: String?) {
        reference!!.child(firebaseChild!!).setValue(userGet[count])
    }

    fun gotoaddWidget(view: View?) {
        val intent = Intent(this@MainMenu, addWidget::class.java)
        startActivity(intent)
    }

    fun gotoremoveWidget(view: View?) {
        val intent = Intent(this@MainMenu, removeWidget::class.java)
        startActivity(intent)
    }

    companion object {
        lateinit var addedWidgetSW: Array<Switch?>
        lateinit var addedWidgetSB: Array<SeekBar?>
        lateinit var addedWidgetTX: Array<TextView?>
    }
}