package com.nedaluof.mihawk.miencryption

import android.content.Context
import com.nedaluof.mihawk.miencryption.ciphermanager.MiAesCipherManager
import com.nedaluof.mihawk.miencryption.model.MiEncryptionSpec

/**
 * Created by NedaluOf on 9/13/2022.
 */
class MiEncryptionImpl(
  context: Context
) : MiEncryption {

  init {
    MiAesCipherManager.init(context)
  }

  override fun initialized(): Boolean =  MiAesCipherManager.isSupported

  override fun encrypt(key: String, plainText: String): MiEncryptionSpec {
    return MiAesCipherManager.encrypt(plainText, key)
  }

  override fun decrypt(miEncryptionSpec: MiEncryptionSpec): String {
    return MiAesCipherManager.decrypt(miEncryptionSpec)
  }
}