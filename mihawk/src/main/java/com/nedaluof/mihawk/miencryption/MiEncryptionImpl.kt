package com.nedaluof.mihawk.miencryption

import android.content.Context
import android.util.Base64
import com.facebook.android.crypto.keychain.AndroidConceal
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain
import com.facebook.crypto.Crypto
import com.facebook.crypto.CryptoConfig
import com.facebook.crypto.Entity

/**
 * Created by NedaluOf on 11/9/2021.
 */
class MiEncryptionImpl(
    context: Context
) : MiEncryption {

    private var crypto: Crypto

    init {
        val keyChain = SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256)
        crypto = AndroidConceal.get().createDefaultCrypto(keyChain)
    }

    override fun initialized() = crypto.isAvailable

    override fun encrypt(key: String, plainText: String): String {
        val entity = Entity.create(key)
        val bytes = crypto.encrypt(plainText.toByteArray(), entity)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    override fun decrypt(key: String, cipherText: String): String {
        val entity = Entity.create(key)
        val decodedCipher = Base64.decode(cipherText, Base64.NO_WRAP)
        val bytes = crypto.decrypt(decodedCipher, entity)
        return String(bytes)
    }
}