package com.example.myapplication7.Camera

import com.example.myapplication7.Camera.ImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadService {
    @Multipart
    @POST("/api/recognize")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): ImageResponse

}