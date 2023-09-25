package com.example.myapplication7.authorization

import retrofit2.http.Body
import retrofit2.http.POST

interface MainApi {
    @POST("/api/auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): UserLogin
}