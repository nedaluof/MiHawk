package com.nedaluof.mihawk.miencryption

import com.nedaluof.mihawk.miencryption.model.MiEncryptionSpec

/**
 * Created by NedaluOf on 11/9/2021.
 */
interface MiEncryption {
  fun initialized(): Boolean
  fun encrypt(key: String, plainText: String): MiEncryptionSpec
  fun decrypt(miEncryptionSpec: MiEncryptionSpec): String
}