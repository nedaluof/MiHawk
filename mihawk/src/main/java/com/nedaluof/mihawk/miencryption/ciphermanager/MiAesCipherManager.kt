package com.nedaluof.mihawk.miencryption.ciphermanager

import com.nedaluof.mihawk.miencryption.model.MiEncryptionSpec

/**
 * Created by NedaluOf on 9/16/2022.
 */
interface MiAesCipherManager {
  /**
   * indicate which if the manager is initialized or not
   * */
  fun isInitialized(): Boolean

  /**
   * encrypt the given @params [plainText] with [key]
   * and @return [MiEncryptionSpec] object.
   * */
  fun encrypt(plainText: String, key: String): MiEncryptionSpec

  /**
   * decrypt the given @params [spec] object
   * and @return [String] of original plainText.
   * */
  fun decrypt(spec: MiEncryptionSpec): String
}