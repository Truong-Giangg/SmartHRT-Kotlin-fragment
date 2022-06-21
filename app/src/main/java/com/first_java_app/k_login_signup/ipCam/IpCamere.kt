package com.first_java_app.k_login_signup.ipCam

import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebView
import android.widget.AutoCompleteTextView
import com.google.firebase.database.FirebaseDatabase
import com.first_java_app.k_login_signup.MainActivity
import android.os.Bundle
import com.first_java_app.k_login_signup.R
import android.widget.ArrayAdapter
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebSettings
import android.widget.Button
import java.util.ArrayList

//https://how2electronics.com/how-to-send-esp32-cam-captured-image-to-google-drive/
class ipCamere : AppCompatActivity() {
    var webView: WebView? = null
    var html: String? = null
    lateinit var start: Button
    lateinit var URL: AutoCompleteTextView
    var arr: MutableList<String?> = ArrayList()
    var myRef = FirebaseDatabase.getInstance().reference.child("users").child(
        MainActivity.user_username_gadget!!
    ).child("IPlist")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ip_camere)
        start = findViewById(R.id.start)
        URL = findViewById(R.id.url)
        webView = findViewById<View>(R.id.webview) as WebView
        getArr()
        URL.setAdapter(adapter)
        start.setOnClickListener(View.OnClickListener {
            val ipCamPath = URL.getText().toString()
            updateIPlist(ipCamPath)
            val imgHtml = "<img src=\"http://$ipCamPath/\">\n"
            html = """<!DOCTYPE html>
<html>
<head>
	<title></title>
</head>
<body>
$imgHtml</body>
</html>"""
            webView!!.settings.javaScriptEnabled = true
            openWebView()
            // hide virtual keyboard
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        })
    }

    private fun updateIPlist(newIp: String) {
        for (a in arr) {
            if (a == newIp) {
                return
            }
        }
        arr.add(newIp)
        myRef.setValue(arr)
    }

    private fun getArr() {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    arr.clear()
                    for (dss in snapshot.children) {
                        val ip = dss.getValue(String::class.java)
                        arr.add(ip)
                    }
                }
                /* 
                StringBuilder stringBuilder=new StringBuilder();
                for (int i=0;i<arr.size();i++){
                    stringBuilder.append(arr.get(i)+"\n");
                }
                Toast.makeText(getApplicationContext(),stringBuilder.toString(),Toast.LENGTH_LONG).show();

                */
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private val adapter: ArrayAdapter<String?>
        private get() = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arr)

    @SuppressLint("NewApi")
    private fun openWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView!!.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        } else {
            webView!!.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        }
        webView!!.loadDataWithBaseURL(
            "file:///android_asset/",
            getHtmlData(html),
            "text/html",
            "utf-8",
            null
        )
    }

    private fun getHtmlData(bodyHTML: String?): String {
        val head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>"
        return "<html>$head<body>$bodyHTML</body></html>"
    }
}