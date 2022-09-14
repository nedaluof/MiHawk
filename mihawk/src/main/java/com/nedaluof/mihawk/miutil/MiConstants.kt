package com.nedaluof.mihawk.miutil

import android.security.keystore.KeyProperties

/**
 * Created by NedaluOf on 11/9/2021.
 */
object MiConstants {

  /**
   * if you already MiHawk USED and the app on
   * the production channel please do not change
   * DEFAULT_PREFERENCE_FILE_NAME cause it
   * will break backward compatibility in terms of
   * keeping previous user data , but if you need to change
   * it use the MiHawk Builder to change it.
   */
  const val DEFAULT_PREFERENCE_FILE_NAME = "MI_HAWK_DEFAULT_PREFERENCE_FILE_NAME"
  /**MiLogger**/
  const val LOG_E = "MiHawk Error -> "
  const val LOG_I = "MiHawk Info -> "
  const val LOG_D = "MiHawk Debug -> "
  /**MiAesCipherManager**/
  const val KEY_LENGTH_BITS = 256
  const val KEY_PROVIDER_NAME = "AndroidKeyStore"
  const val KEYGEN_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
  const val KEYGEN_BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
  const val KEYGEN_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
  const val CIPHER_ALGORITHM = "$KEYGEN_ALGORITHM/$KEYGEN_BLOCK_MODE/$KEYGEN_PADDING"

}