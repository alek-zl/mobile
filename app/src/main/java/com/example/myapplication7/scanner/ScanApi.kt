package com.example.myapplication7.scanner

import com.example.myapplication7.authorization.AuthRequest
import com.example.myapplication7.authorization.UserLogin
import retrofit2.http.Body
import retrofit2.http.POST

interface ScanApi {
    @POST("/api/search")
    suspend fun auth(@Body scanRequest: ScanRequest): ScanAnswer
}