package com.example.newproject.network.model.request

data class VerifyOtpRequest(
    val phone: String,
    val otp: String,
    val deviceId: String
)
