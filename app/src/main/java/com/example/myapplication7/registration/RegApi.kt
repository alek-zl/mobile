package com.example.myapplication7.registration


import retrofit2.http.Body
import retrofit2.http.POST

interface RegApi {
    @POST("/api/auth/register")
    suspend fun auth(@Body regRequest: RegRequest): UserReg
}