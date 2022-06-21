package com.first_java_app.k_login_signup.fragment
//
//import android.Manifest
//import com.first_java_app.k_login_signup.objectDetectorClass.recognizeImage
//import com.first_java_app.k_login_signup.UserHelperClassGadget.widType
//import com.first_java_app.k_login_signup.UserHelperClassGadget.gestureT
//import android.app.Activity
//import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
//import org.opencv.core.Mat
//import org.opencv.android.CameraBridgeViewBase
//import com.first_java_app.k_login_signup.objectDetectorClass
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.DatabaseReference
//import com.first_java_app.k_login_signup.UserHelperClassGadget
//import android.widget.TextView
//import org.opencv.android.BaseLoaderCallback
//import org.opencv.android.LoaderCallbackInterface
//import android.os.Bundle
//import android.view.WindowManager
//import androidx.core.content.ContextCompat
//import android.content.pm.PackageManager
//import androidx.core.app.ActivityCompat
//import com.first_java_app.k_login_signup.R
//import android.content.pm.ActivityInfo
//import android.view.SurfaceView
//import com.first_java_app.k_login_signup.MainActivity
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import org.opencv.android.OpenCVLoader
//import org.opencv.core.CvType
//import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
//import android.widget.Toast
//import android.content.Intent
//import android.os.Handler
//import android.util.Log
//import android.view.View
//import android.view.Window
//import androidx.fragment.app.Fragment
//import com.first_java_app.k_login_signup.MainMenu
//import com.first_java_app.k_login_signup.addGesture
//import java.io.IOException
//
//class CameraActivityFragment : Fragment(), CvCameraViewListener2 {
//    private var mRgba: Mat? = null
//    private var mGray: Mat? = null
//    private var mOpenCvCameraView: CameraBridgeViewBase? = null
//    private var objectDetectorClass: objectDetectorClass? = null
//    private var preAlpha: String? = null
//    private var alpha: String? = null
//    val handler = Handler()
//    val mHandler = Handler()
//    val delay = 2000 //milliseconds
//    var rootNode: FirebaseDatabase? = null
//    var reference: DatabaseReference? = null
//    lateinit var userGet: Array<UserHelperClassGadget?>
//    var size = 0
//    var textPredict: TextView? = null
//    val mshowPredict = Runnable { showPredict() }
//    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
//        override fun onManagerConnected(status: Int) {
//            when (status) {
//                SUCCESS -> {
//                    run {
//                        Log.i(TAG, "OpenCv Is loaded")
//                        mOpenCvCameraView!!.enableView()
//                    }
//                    run { super.onManagerConnected(status) }
//                }
//                else -> {
//                    super.onManagerConnected(status)
//                }
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//        val MY_PERMISSIONS_REQUEST_CAMERA = 0
//        // if camera permission is not given it will ask for it on device
//        if (ContextCompat.checkSelfPermission(this@CameraActivityFragment, Manifest.permission.CAMERA)
//            == PackageManager.PERMISSION_DENIED
//        ) {
//            ActivityCompat.requestPermissions(
//                this@CameraActivityFragment,
//                arrayOf(Manifest.permission.CAMERA),
//                MY_PERMISSIONS_REQUEST_CAMERA
//            )
//        }
//        setContentView(R.layout.activity_main_gesture)
//        textPredict = findViewById(R.id.showPredict)
//        mOpenCvCameraView = findViewById<View>(R.id.frame_Surface) as CameraBridgeViewBase
//        this@CameraActivityFragment.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//        mOpenCvCameraView!!.visibility = SurfaceView.VISIBLE
//        mOpenCvCameraView!!.setCameraPermissionGranted() // must have since opencv version 4.xx or higher
//        mOpenCvCameraView!!.setCvCameraViewListener(this)
//        try {
//            objectDetectorClass = objectDetectorClass(
//                assets,
//                "hand_model.tflite",
//                "hand_label.txt",
//                300,
//                "hand_gesture_model.tflite",
//                96
//            )
//            Log.d("MainActivity", "Model is successfully loaded")
//        } catch (e: IOException) {
//            Log.d("MainActivity", "Getting some error")
//            e.printStackTrace()
//        }
//        rootNode = FirebaseDatabase.getInstance()
//        reference = rootNode!!.reference.child("users").child(MainActivity.user_username_gadget)
//            .child("user's gadget")
//        reference!!.addListenerForSingleValueEvent(object : ValueEventListener {
//            //get data from firebase only once
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                var userNum = 0
//                size = dataSnapshot.childrenCount.toInt()
//                userGet = arrayOfNulls(size)
//                for (snapshot in dataSnapshot.children) {
//                    userGet[userNum] = snapshot.getValue(
//                        UserHelperClassGadget::class.java
//                    ) //get data store to class
//                    userNum++
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })
//        handler.postDelayed(object : Runnable {
//            override fun run() {
//                //Toast.makeText(CameraActivity.this, "handle loop:"+alpha, Toast.LENGTH_SHORT).show();
//                if (preAlpha != null && alpha != null) {
//                    if (preAlpha == alpha) {
//                        //Toast.makeText(CameraActivity.this, "du 2 s:"+alpha, Toast.LENGTH_SHORT).show();
//                        checkAndPushData()
//                    }
//                }
//                preAlpha = alpha
//                handler.postDelayed(this, delay.toLong())
//            }
//        }, delay.toLong())
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (OpenCVLoader.initDebug()) {
//            //if load success
//            Log.d(TAG, "Opencv initialization is done")
//            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
//        } else {
//            //if not loaded
//            Log.d(TAG, "Opencv is not loaded. try again")
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback)
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (mOpenCvCameraView != null) {
//            mOpenCvCameraView!!.disableView()
//        }
//    }
//
//    public override fun onDestroy() {
//        super.onDestroy()
//        if (mOpenCvCameraView != null) {
//            mOpenCvCameraView!!.disableView()
//        }
//    }
//
//    override fun onCameraViewStarted(width: Int, height: Int) {
//        mRgba = Mat(height, width, CvType.CV_8UC4)
//        mGray = Mat(height, width, CvType.CV_8UC1)
//    }
//
//    override fun onCameraViewStopped() {
//        mRgba!!.release()
//    }
//
//    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat {
//        mRgba = inputFrame.rgba()
//        mGray = inputFrame.gray()
//
//        //call recognizeImage
//        var out = Mat()
//        out = objectDetectorClass!!.recognizeImage(mRgba)
//        //        System.out.println("bien"+objectDetectorClass.alphaOut);
//        alpha = objectDetectorClass!!.alphaOut
//        mHandler.post(mshowPredict)
//        return out
//    }
//
//    fun showPredict() {
//        textPredict!!.text = "chưa thêm cử chỉ!!"
//        for (i in 0 until size) {
//            if (userGet[i]!!.widType == "button") {
//                if (alpha != null) {
//                    if (alpha == userGet[i]!!.gestureT!![0].toString()) {
//                        textPredict!!.text = "giữ 2 giây để bật " + userGet[i]!!.btnName
//                    } else if (alpha == userGet[i]!!.gestureT!![1].toString()) {
//                        textPredict!!.text = "giữ 2 giây để tắt " + userGet[i]!!.btnName
//                    }
//                } else textPredict!!.text = "chưa phát hiện tay!!"
//            }
//        }
//    }
//
//    fun checkAndPushData() {
//        for (i in 0 until size) {
//            if (userGet[i]!!.widType == "button") {
//                if (alpha == userGet[i]!!.gestureT!![0].toString()) {
//                    Toast.makeText(
//                        this@CameraActivityFragment,
//                        "đã bật: " + userGet[i]!!.btnName,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    userGet[i]!!.btnValue = "1"
//                    reference!!.child(i.toString()).setValue(userGet[i])
//                } else if (alpha == userGet[i]!!.gestureT!![1].toString()) {
//                    Toast.makeText(
//                        this@CameraActivityFragment,
//                        "đã tắt: " + userGet[i]!!.btnName,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    userGet[i]!!.btnValue = "0"
//                    reference!!.child(i.toString()).setValue(userGet[i])
//                }
//            }
//        }
//    }
//
//    fun backHome(view: View?) {
//        val intent = Intent(this@CameraActivityFragment, MainMenu::class.java)
//        startActivity(intent)
//    }
//
//    fun goAddGesture(view: View?) {
//        val intent = Intent(this@CameraActivityFragment, addGesture::class.java)
//        startActivity(intent)
//    }
//
//    fun goSwitchGesture(view: View?) {
//        val intent = Intent(this@CameraActivityFragment, switchGesture::class.java)
//        startActivity(intent)
//    }
//
//    companion object {
//        private const val TAG = "MainActivity"
//    }
//
//    init {
//        Log.i(TAG, "Instantiated new " + this.javaClass)
//    }
//}