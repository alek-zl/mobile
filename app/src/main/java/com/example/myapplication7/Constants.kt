package com.example.myapplication7

import android.Manifest

object Constants {
    const val REQUEST_CODE_DESCRIPTION = 100
    const val REQUEST_CODE_LOGIN = 200
    const val REQUEST_CODE_REG = 300
    const val REQUEST_CODE_CAMERA = 400
    //CameraX
    const val TAG = "cameraX"
    const val FILE_NAME_FORMAT ="yy-MM-dd-HH-mm-SSS"
    const val REQUEST_CODE_PERMISSIONS = 123
    val REQUEST_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    //Base URL
    const val BASE_URL = "https://210c-2a03-d000-1581-b799-7023-11bf-6488-b58f.ngrok-free.app/"
}