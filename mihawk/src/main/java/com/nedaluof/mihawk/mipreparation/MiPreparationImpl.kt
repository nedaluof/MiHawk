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
    val miEncryptionSpec = try {
      serializer.fromString(cipherText, MiEncryptionSpec::class.java)
    } catch (e: Exception) {
      null
    }
    if (miEncryptionSpec != null && encryptor.initialized()) {
      val plainText = encryptor.decrypt(miEncryptionSpec)
      if (plainText.isNotEmpty()) {
        return serializer.fromString(plainText, aClass)
      }
    }
    return try {
      serializer.fromString(cipherText, aClass)
    } catch (e: Exception) {
      null
    }
  }

  override fun <T> prepareDataToGet(
    rawValue: Any,
    aClass: Class<T>
  ): T? {
    return when {
      rawValue is String -> prepareDataToGet(rawValue, aClass)
      aClass.isInstance(rawValue) -> aClass.cast(rawValue)
      else -> try {
        serializer.fromString(serializer.toString(rawValue), aClass)
      } catch (e: Exception) {
        null
      }
    }
  }
}