package com.nedaluof.mihawk.miencryption

/**
 * Created by NedaluOf on 11/9/2021.
 */
interface MiEncryption {
    fun initialized(): Boolean
    fun encrypt(key: String, plainText: String): String
    fun decrypt(key: String, cipherText: String): String
}