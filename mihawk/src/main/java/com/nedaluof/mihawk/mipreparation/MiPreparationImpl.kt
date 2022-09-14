package com.nedaluof.mihawk.mipreparation

import com.nedaluof.mihawk.miencryption.MiEncryption
import com.nedaluof.mihawk.miencryption.model.MiEncryptionSpec
import com.nedaluof.mihawk.miserializer.MiSerializer

/**
 * Created by NedaluOf on 12/6/2021.
 */
class MiPreparationImpl(
  private val serializer: MiSerializer,
  private val encryptor: MiEncryption
) : MiPreparation {

  override fun <T> prepareDataToSet(
    key: String,
    value: T
  ): String {
    val serializedValue = serializer.toString(value)
    val miSpec: MiEncryptionSpec
    var cipher = ""
    if (encryptor.initialized()) {
      miSpec = encryptor.encrypt(key, serializedValue)
      val miSpecSerialized = serializer.toString(miSpec)
      cipher = miSpecSerialized
    }
    return cipher.ifEmpty { serializedValue }
  }

  override fun <T> prepareDataToGet(
    cipherText: String,
    aClass: Class<T>
  ): T? {
    val miEncryptionSpec = serializer.fromString(cipherText, MiEncryptionSpec::class.java)
    var plainText = ""
    if (encryptor.initialized()) {
      if (miEncryptionSpec != null) {
        plainText = encryptor.decrypt(miEncryptionSpec)
      }
    }
    return if (plainText.isNotEmpty()) serializer.fromString(plainText, aClass) else null
  }
}