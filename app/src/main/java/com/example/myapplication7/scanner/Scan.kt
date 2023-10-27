package com.example.myapplication7.scanner

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.example.myapplication7.Constants
import com.example.myapplication7.R
import com.example.myapplication7.authorization.AuthRequest
import com.example.myapplication7.authorization.MainApi
import com.example.myapplication7.databinding.ActivityScanBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Scan : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    var intentData = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }



    private fun iniBc(){


        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920,1080)
            .setAutoFocusEnabled(true)
            //.setFacing(CameraSource.CAMERA_FACING_FRONT)
            .build()
        binding.surfaceView!!.holder.addCallback(object : SurfaceHolder.Callback{
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource.start(binding.surfaceView!!.holder)
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }

        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode>{
            override fun release() {
                /*Toast.makeText(applicationContext, "barcode scanner has been stooprd", Toast.LENGTH_SHORT).show()*/
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if(barcodes.size()!=0){
                    intentData = barcodes.valueAt(0).displayValue
                    intent.putExtra("barcode", intentData)
                    setResult(555, intent)
                    finish()


                }
            }


        })
    }



    override fun onPause(){
        super.onPause()
        cameraSource!!.release()
    }



    override fun onResume() {
        super.onResume()
        iniBc()
    }
}