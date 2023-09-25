package com.example.myapplication7

import android.Manifest

object Costants {
    const val REQUEST_CODE_DESCRIPTION = 100
    const val REQUEST_CODE_LOGIN = 200
    const val REQUEST_CODE_REG = 300
    const val REQUEST_CODE_CAMERA = 400
    //CameraX
    const val TAG = "cameraX"
    const val FILE_NAME_FORMAT ="yy-MM-dd-HH-mm-SSS"
    const val REQUEST_CODE_PERMISSIONS = 123
    val REQUEST_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
}