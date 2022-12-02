package com.example.auth

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

val hashKey = hex(System.getenv("SECRET_KEY")) // convert to hex string.
val hmacKey = SecretKeySpec(hashKey,"HmacSHA1") //secured hash algorithm to produce 160 bit hash value.

fun hashPassword(password:String):String{
    // Message Authentication Codes
    val hMac = Mac.getInstance("HmacSHA1") //MAC are used between two parties that share a secret key to validate info. transmitted between them.
    hMac.init(hmacKey) // init MAC object with given key
    return hex(hMac.doFinal(password.toByteArray(Charsets.UTF_8)))
}