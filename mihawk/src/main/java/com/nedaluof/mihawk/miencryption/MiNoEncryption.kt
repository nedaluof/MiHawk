package com.nedaluof.mihawk.miencryption

import android.util.Base64

/**
 * Created by NedaluOf on 11/9/2021.
 */
class MiNoEncryption : MiEncryption {
    override fun initialized() = true

    override fun encrypt(key: String, plainText: String): String =
        Base64.encodeToString(plainText.toByteArray(), Base64.DEFAULT)

    override fun decrypt(key: String, cipherText: String): String =
        String(Base64.decode(cipherText, Base64.DEFAULT))
}