package com.narbase.pss.main.sms

@Suppress("ArrayInDataClass")
data class SmsMessageData(
    val message: String,
    val phones: Array<Long>
)
