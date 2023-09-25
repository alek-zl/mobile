package com.example.myapplication7.Camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileResults
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication7.databinding.ActivityCameraBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


import com.example.myapplication7.Constants
import com.example.myapplication7.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




class Camera : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture:ImageCapture?=null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor:ExecutorService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        if(allPermissionGranted()){
            startCamera()
        }
        else{
            ActivityCompat.requestPermissions(this,
                Constants.REQUEST_PERMISSIONS,
                Constants.REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnTakePhoto.setOnClickListener{
            takePhoto()
            setResult(RESULT_OK, intent)
            finish()
        }

    }

    private fun getOutputDirectory(): File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let{mFile->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }

        return if (mediaDir !=null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun takePhoto(){

        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(Constants.FILE_NAME_FORMAT, Locale.getDefault())
                .format(System
                    .currentTimeMillis())+".jpg")
        val outputOption = ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()

        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(this),
            object :  ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: OutputFileResults){
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo Saved"

                    Toast.makeText(this@Camera,
                        "$msg $savedUri",
                        Toast.LENGTH_LONG)
                        .show()


                }

                override fun onError(exception: ImageCaptureException){
                    Log.e(Constants.TAG, "onError: ${exception.message}", exception)
                }

            }
        )


    }

    private fun upload(imageUri: Uri){
        val filesDir = applicationContext.filesDir
        val file  = File(filesDir, "image.png")
        val inputSteam = contentResolver.openInputStream(imageUri)
        val outputSteam = FileOutputStream(file)
        inputSteam!!.copyTo(outputSteam)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", file.name, requestBody)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })

        val retrofit =
            Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(UploadService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.uploadImage(part)
            Log.d("FORSEFULCODE", response.toString())
            runOnUiThread{

                Toast.makeText(this@Camera,
                    "Фото отправлено!!! ${response.text}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun startCamera(){

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also { mPreview ->
                    mPreview.setSurfaceProvider(
                        binding.viewFinder.surfaceProvider
                    )
                }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            }catch (e:Exception){
                Log.d(Constants.TAG, "startCamera Fail", e)
            }
        }, ContextCompat.getMainExecutor(this))

    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.REQUEST_CODE_PERMISSIONS){
            if(allPermissionGranted()){
                startCamera()
            }else{
                Toast.makeText(this, "Permission not granted by user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() =
        Constants.REQUEST_PERMISSIONS.all{
            ContextCompat.checkSelfPermission(baseContext, it)==PackageManager.PERMISSION_GRANTED
        }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}