package com.nedaluof.mihawk.miencryption.model

/**
 * Created by NedaluOf on 9/13/2022.
 * All values of this class are Base64 encoded
 * to ensure a safe Java String representation.
 */
class MiEncryptionSpec(
  val cipherText: String = "",
  val key: String = ""
)