package com.nedaluof.mihawk.mipreparation

import com.nedaluof.mihawk.miencryption.MiEncryption
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
    var cipher = ""
    if (encryptor.initialized()) {
      cipher = encryptor.encrypt(key, serializedValue)
    }
    return cipher.ifEmpty { serializedValue }
  }

  override fun <T> prepareDataToGet(
    keyAndCipherText: Pair<String, String>,
    aClass: Class<T>
  ): T? {
    val key = keyAndCipherText.first
    val cipherText = keyAndCipherText.second

    var plainText = ""
    if (encryptor.initialized()) {
      plainText = encryptor.decrypt(key, cipherText)
    }
    return if (plainText.isNotEmpty()) serializer.fromString(plainText, aClass) else null
  }
}