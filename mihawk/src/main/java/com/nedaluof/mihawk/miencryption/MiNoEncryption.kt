package com.nedaluof.mihawk.miencryption

import android.util.Base64
import com.nedaluof.mihawk.miencryption.model.MiEncryptionSpec

/**
 * Created by NedaluOf on 11/9/2021.
 */
class MiNoEncryption : MiEncryption {

  override fun initialized() = true

  override fun encrypt(key: String, plainText: String): MiEncryptionSpec =
    MiEncryptionSpec(Base64.encodeToString(plainText.toByteArray(), Base64.DEFAULT), key)

  override fun decrypt(miEncryptionSpec: MiEncryptionSpec): String =
    String(Base64.decode(miEncryptionSpec.cipherText, Base64.DEFAULT))
}