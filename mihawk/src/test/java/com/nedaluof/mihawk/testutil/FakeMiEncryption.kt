package com.nedaluof.mihawk.testutil

import com.nedaluof.mihawk.miencryption.MiEncryption
import com.nedaluof.mihawk.miencryption.model.MiEncryptionSpec

/**
 * Reversible fake of [MiEncryption] that needs no Android Keystore or Base64,
 * so it is safe for JVM unit tests.
 */
class FakeMiEncryption(
  private val isInitialized: Boolean = true
) : MiEncryption {

  override fun initialized() = isInitialized

  override fun encrypt(key: String, plainText: String): MiEncryptionSpec =
    MiEncryptionSpec(cipherText = plainText.reversed(), key = key)

  override fun decrypt(miEncryptionSpec: MiEncryptionSpec): String =
    miEncryptionSpec.cipherText.reversed()
}
