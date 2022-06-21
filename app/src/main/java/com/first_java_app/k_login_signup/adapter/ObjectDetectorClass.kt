package com.first_java_app.k_login_signup.adapter

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.*

class objectDetectorClass internal constructor(
    assetManager: AssetManager,
    modelPath: String,
    labelPath: String,
    private val INPUT_SIZE: Int,
    classification_model: String,
    private val Classification_Input_Size: Int
) {
    // should start from small letter
    // this is used to load hand model and predict
    private val interpreter: Interpreter

    // create another interpreter for sign_language_model
    private val interpreter2: Interpreter

    // this will load sign_language_model
    // store all label in array
    private val labelList: List<String>
    private val PIXEL_SIZE = 3 // for RGB
    private val IMAGE_MEAN = 0
    private val IMAGE_STD = 255.0f

    // use to initialize gpu in app
    private val gpuDelegate: GpuDelegate
    private var height = 0
    private var width = 0
    private val defaultGesture = "chưa cài đặt"
    var alphaOut: String? = null
    @Throws(IOException::class)
    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        // to store label
        val labelList: MutableList<String> = ArrayList()
        // create a new reader
        val reader = BufferedReader(InputStreamReader(assetManager.open(labelPath)))
        var line: String = "hand"
        // loop through each line and store it to labelList
//        while (reader.readLine().also { line = it } != null) {
//            labelList.add(line)
//        }

        labelList.add(line)
        reader.close()
        return labelList
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): ByteBuffer {
        // use to get description of file
        val fileDescriptor: AssetFileDescriptor = assetManager.openFd(modelPath)
        val inputStream: FileInputStream = FileInputStream(fileDescriptor.getFileDescriptor())
        val fileChannel = inputStream.channel
        val startOffset: Long = fileDescriptor.getStartOffset()
        val declaredLength: Long = fileDescriptor.getDeclaredLength()
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    // create new Mat function
    fun recognizeImage(mat_image: Mat): Mat {
        // Rotate original image by 90 degree get get portrait frame
        // This will fix crashing problem of the app
        var rotated_mat_image = Mat()
        //Mat a=mat_image.t();
        //Core.flip(a,rotated_mat_image,1);
        // Release mat
        //a.release();
        rotated_mat_image = mat_image // not rotate for landscape

        // if you do not do this process you will get improper prediction, less no. of object
        // now convert it to bitmap
        var bitmap: Bitmap? = null
        bitmap = Bitmap.createBitmap(
            rotated_mat_image.cols(),
            rotated_mat_image.rows(),
            Bitmap.Config.ARGB_8888
        )
        Utils.matToBitmap(rotated_mat_image, bitmap)
        // define height and width
        height = bitmap.getHeight()
        width = bitmap.getWidth()

        // scale the bitmap to input size of model
        val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)

        // convert bitmap to bytebuffer as model input should be in it
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)

        // defining output
        // 10: top 10 object detected
        // 4: there coordinate in image
        //  float[][][]result=new float[1][10][4];
        val input = arrayOfNulls<Any>(1)
        input[0] = byteBuffer
        val output_map: MutableMap<Int, Any> = TreeMap()
        // we are not going to use this method of output
        // instead we create treemap of three array (boxes,score,classes)
        val boxes = Array(1) { Array(10) { FloatArray(4) } }
        // 10: top 10 object detected
        // 4: there coordinate in image
        val scores = Array(1) { FloatArray(10) }
        // stores scores of 10 object
        val classes = Array(1) { FloatArray(10) }
        // stores class of object

        // add it to object_map;
        output_map[0] = boxes
        output_map[1] = classes
        output_map[2] = scores

        // now predict
        interpreter.runForMultipleInputsOutputs(input, output_map)
        //      1. Loading tensorflow lite model
        //      2. Predicting object
        // we will draw boxes and label it with it's name
        val value = output_map[0]
        val Object_class = output_map[1]
        val score = output_map[2]

        // loop through each object
        // as output has only 10 boxes
        alphaOut = null
        for (i in 0..9) {
            // for here we are looping through each hand which is detected
            val class_value = java.lang.reflect.Array.get(
                java.lang.reflect.Array.get(Object_class, 0),
                i
            ) as Float
            val score_value =
                java.lang.reflect.Array.get(java.lang.reflect.Array.get(score, 0), i) as Float
            // define threshold for score

            // Here you can change threshold according to your model
            // Now we will do some change to improve app
            if (score_value > 0.5) {
                val box1 = java.lang.reflect.Array.get(java.lang.reflect.Array.get(value, 0), i)
                // we are multiplying it with Original height and width of frame

                // (x1,y1) is the starting point of hand
                // (x2,y2) is the end point of hand
                var y1 = java.lang.reflect.Array.get(box1, 0) as Float * height
                var x1 = java.lang.reflect.Array.get(box1, 1) as Float * width
                var y2 = java.lang.reflect.Array.get(box1, 2) as Float * height
                var x2 = java.lang.reflect.Array.get(box1, 3) as Float * width

                // set boundary limit
                if (x1 < 0) x1 = 0f
                if (y1 < 0) y1 = 0f
                if (x2 > width) x2 = width.toFloat()
                if (y2 > height) y2 = height.toFloat()
                // set height and width of box
                val w1 = x2 - x1
                val h1 = y2 - y1
                // crop hand image from original frame
                val cropped_roi = Rect(x1.toInt(), y1.toInt(), w1.toInt(), h1.toInt())
                val cropped: Mat = Mat(rotated_mat_image, cropped_roi).clone()
                // now convert this cropped Mat to Bitmap
                var bitmap1: Bitmap? = null
                bitmap1 =
                    Bitmap.createBitmap(cropped.cols(), cropped.rows(), Bitmap.Config.ARGB_8888)
                Utils.matToBitmap(cropped, bitmap1)
                // resize bitmap1 to classification input size = 96
                val scaledBitmap1: Bitmap = Bitmap.createScaledBitmap(
                    bitmap1,
                    Classification_Input_Size,
                    Classification_Input_Size,
                    false
                )
                // convert scaledBitmap1 to byte buffer
                val byteBuffer1 = convertBitmapToByteBuffer1(scaledBitmap1)
                // create an array for output of interpreter2
                val output_class_value = Array(1) { FloatArray(1) }

                // predict output for byteBuffer1
                interpreter2.run(byteBuffer1, output_class_value)
                Log.d("objectDetectionClass", "output_class_value:" + output_class_value[0][0])

                // convert output_class_value to alphabet
                val sign_val = getAlphabets(output_class_value[0][0])
                alphaOut = sign_val
                //              input/output        text            starting point              font size
                //Imgproc.putText(rotated_mat_image,""+sign_val,new Point(x1+10,y1+40),2,1.5,new Scalar(255, 255, 255, 255),2);
                Imgproc.putText(
                    rotated_mat_image,
                    "" + sign_val,
                    Point((x1 + 10).toDouble(), (y1 + 40).toDouble()),
                    2,
                    1.5,
                    Scalar(255.0, 255.0, 255.0, 255.0),
                    2
                )
                Imgproc.rectangle(
                    rotated_mat_image, Point(x1.toDouble(), y1.toDouble()), Point(
                        x2.toDouble(), y2.toDouble()
                    ), Scalar(0.0, 255.0, 0.0, 255.0), 2
                )
            }
        }

        // before returning rotate back by -90 degree
        // Do same here
        //Mat b=rotated_mat_image.t();
        //Core.flip(b,mat_image,0);
        //b.release();
        //return mat_image;
        return rotated_mat_image
    }

    private fun getAlphabets(sign_v: Float): String {
        var `val` = ""
        `val` = if (sign_v >= -0.5 && sign_v < 0.5) {
            "A"
        } else if (sign_v >= 0.5 && sign_v < 1.5) {
            "B"
        } else if (sign_v >= 1.5 && sign_v < 2.5) {
            "C"
        } else if (sign_v >= 2.5 && sign_v < 3.5) {
            "D"
        } else if (sign_v >= 3.5 && sign_v < 4.5) {
            "E"
        } else if (sign_v >= 4.5 && sign_v < 5.5) {
            "F"
        } else if (sign_v >= 5.5 && sign_v < 6.5) {
            "G"
        } else if (sign_v >= 6.5 && sign_v < 7.5) {
            "H"
        } else if (sign_v >= 7.5 && sign_v < 8.5) {
            "I"
        } else if (sign_v >= 8.5 && sign_v < 9.5) {
            "J"
        } else if (sign_v >= 9.5 && sign_v < 10.5) {
            "K"
        } else if (sign_v >= 10.5 && sign_v < 11.5) {
            "L"
        } else if (sign_v >= 11.5 && sign_v < 12.5) {
            "M"
        } else if (sign_v >= 12.5 && sign_v < 13.5) {
            "N"
        } else if (sign_v >= 13.5 && sign_v < 14.5) {
            "O"
        } else if (sign_v >= 14.5 && sign_v < 15.5) {
            "P"
        } else if (sign_v >= 15.5 && sign_v < 16.5) {
            "Q"
        } else if (sign_v >= 16.5 && sign_v < 17.5) {
            "R"
        } else if (sign_v >= 17.5 && sign_v < 18.5) {
            "S"
        } else if (sign_v >= 18.5 && sign_v < 19.5) {
            "T"
        } else if (sign_v >= 19.5 && sign_v < 20.5) {
            "U"
        } else if (sign_v >= 20.5 && sign_v < 21.5) {
            "V"
        } else if (sign_v >= 21.5 && sign_v < 22.5) {
            "W"
        } else if (sign_v >= 22.5 && sign_v < 23.5) {
            "X"
        } else "Y"
        return `val`
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer: ByteBuffer
        // some model input should be quant=0  for some quant=1
        // for this quant=0
        // Change quant=1
        // As we are scaling image from 0-255 to 0-1
        val quant = 1
        val size_images = INPUT_SIZE
        byteBuffer = if (quant == 0) {
            ByteBuffer.allocateDirect(1 * size_images * size_images * 3)
        } else {
            ByteBuffer.allocateDirect(4 * 1 * size_images * size_images * 3)
        }
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(size_images * size_images)
        bitmap.getPixels(
            intValues,
            0,
            bitmap.getWidth(),
            0,
            0,
            bitmap.getWidth(),
            bitmap.getHeight()
        )
        var pixel = 0

        // some error
        //now run
        for (i in 0 until size_images) {
            for (j in 0 until size_images) {
                val `val` = intValues[pixel++]
                if (quant == 0) {
                    byteBuffer.put((`val` shr 16 and 0xFF).toByte())
                    byteBuffer.put((`val` shr 8 and 0xFF).toByte())
                    byteBuffer.put((`val` and 0xFF).toByte())
                } else {
                    // paste this
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) / 255.0f)
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) / 255.0f)
                    byteBuffer.putFloat((`val` and 0xFF) / 255.0f)
                }
            }
        }
        return byteBuffer
    }

    private fun convertBitmapToByteBuffer1(bitmap: Bitmap): ByteBuffer {
        val byteBuffer: ByteBuffer
        val quant = 1
        val size_images = Classification_Input_Size
        byteBuffer = if (quant == 0) {
            ByteBuffer.allocateDirect(1 * size_images * size_images * 3)
        } else {
            ByteBuffer.allocateDirect(4 * 1 * size_images * size_images * 3)
        }
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(size_images * size_images)
        bitmap.getPixels(
            intValues,
            0,
            bitmap.getWidth(),
            0,
            0,
            bitmap.getWidth(),
            bitmap.getHeight()
        )
        var pixel = 0
        // remove 255.0f as we did not scale the image
        for (i in 0 until size_images) {
            for (j in 0 until size_images) {
                val `val` = intValues[pixel++]
                if (quant == 0) {
                    byteBuffer.put((`val` shr 16 and 0xFF).toByte())
                    byteBuffer.put((`val` shr 8 and 0xFF).toByte())
                    byteBuffer.put((`val` and 0xFF).toByte())
                } else {
                    byteBuffer.putFloat((`val` shr 16 and 0xFF).toFloat())
                    byteBuffer.putFloat((`val` shr 8 and 0xFF).toFloat())
                    byteBuffer.putFloat((`val` and 0xFF).toFloat())
                }
            }
        }
        return byteBuffer
    }

    init {
        // use to define gpu or cpu // no. of threads
        val options: Interpreter.Options = Interpreter.Options()
        gpuDelegate = GpuDelegate()
        options.addDelegate(gpuDelegate)
        options.setNumThreads(4) // set it according to your phone
        // loading model
        interpreter = Interpreter(loadModelFile(assetManager, modelPath), options)
        // load labelmap
        labelList = loadLabelList(assetManager, labelPath)

        // code for load model
        val options2: Interpreter.Options = Interpreter.Options()
        // add 2 thread into this interpreter
        options2.setNumThreads(2)
        // load model
        interpreter2 = Interpreter(loadModelFile(assetManager, classification_model), options2)
    }
}