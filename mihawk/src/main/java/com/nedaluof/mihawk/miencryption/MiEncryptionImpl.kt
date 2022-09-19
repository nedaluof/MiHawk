package com.nedaluof.mihawk.miencryption

import com.nedaluof.mihawk.miencryption.ciphermanager.MiAesCipherManager
import com.nedaluof.mihawk.miencryption.ciphermanager.MiAesCipherManagerImpl
import com.nedaluof.mihawk.miencryption.model.MiEncryptionSpec

/**
 * Created by NedaluOf on 9/13/2022.
 */
class MiEncryptionImpl(
  private val miAesCipherManager: MiAesCipherManager
) : MiEncryption {

  override fun initialized(): Boolean =  miAesCipherManager.isInitialized()

  override fun encrypt(key: String, plainText: String): MiEncryptionSpec {
    return miAesCipherManager.encrypt(plainText, key)
  }

  override fun decrypt(miEncryptionSpec: MiEncryptionSpec): String {
    return miAesCipherManager.decrypt(miEncryptionSpec)
  }
}