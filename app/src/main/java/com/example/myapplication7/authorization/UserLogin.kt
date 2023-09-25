package com.example.myapplication7.authorization

data class UserLogin(
    val isUserInBD: Boolean,
    val verificationCode: Int,
    val accessToken: String
)
