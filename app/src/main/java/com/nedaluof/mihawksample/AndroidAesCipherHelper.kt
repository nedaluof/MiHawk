package com.nedaluof.mihawksample

/**
 * Created by NedaluOf on 6/11/2022.
 */
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec

/**
 * String encryption/decryption helper with system generated key.
 *
 * Although using constant IV in CBC mode produces more more resilient results compare to ECB/CTR
 * modes, it could increase chances of statistical crypto analysis attack and is not a good practice.
 * Therefore, you must manage pairs of encrypted results and IV together to decrypt correctly.
 *
 * This class is marked as "Targeted for API Level 23+". Since system generated symmetric key is
 * unfortunately, only supported in Android API Level 23+; For API Level 18 to 22, another key
 * protection mechanism is required, such as a combination with asymmetric keys provided by
 * Android KeyStore system.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Jun - 2018
 */
@TargetApi(Build.VERSION_CODES.M)
object AndroidAesCipherHelper {
  private const val TAG = "AndroidAesCipherHelper"
  private const val KEY_LENGTH_BITS = 256

  private const val KEY_PROVIDER_NAME = "AndroidKeyStore"
  private const val KEYGEN_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
  private const val KEYGEN_BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
  private const val KEYGEN_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
  private const val CIPHER_ALGORITHM = "$KEYGEN_ALGORITHM/$KEYGEN_BLOCK_MODE/$KEYGEN_PADDING"

  private lateinit var keyEntry: KeyStore.SecretKeyEntry

  // Private only backing fields
  @Suppress("ObjectPropertyName")
  private lateinit var _keygen: KeyGenerator

  // Private only backing fields
  @Suppress("ObjectPropertyName")
  private var _isSupported = false

  val isSupported: Boolean
    get() = _isSupported

  internal fun init(applicationContext: Context) {
    if (_isSupported) {
      Log.w(TAG, "Already initialised - Do not attempt to initialise this twice")
      return
    }

    try {
      this._keygen = KeyGenerator.getInstance(KEYGEN_ALGORITHM, KEY_PROVIDER_NAME)
    } catch (e: GeneralSecurityException) {
      this._isSupported = false
      // Nonsense, but happens on low-end devices such as Xiaomi
      Log.w(TAG, "It seems that this device does not supports AES and/or RSA encryption.")
      return
    }

    val alias = "${applicationContext.packageName}.aeskey"
    val keyStore = KeyStore.getInstance(KEY_PROVIDER_NAME).apply {
      load(null)
    }

    val result = if (keyStore.containsAlias(alias)) {
      Log.v(TAG, "Secret key for %s exists, loading previously created one , $alias")
      true
    } else {
      Log.v(TAG, "No secret key for %s, creating a new one, $alias")
      initAndroidM(alias)
    }

    this.keyEntry = keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry
    this._isSupported = result
  }

  private fun initAndroidM(alias: String): Boolean {
    return try {
      with(_keygen) {
        init(
          KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
          )
            .setKeySize(KEY_LENGTH_BITS)
            .setBlockModes(KEYGEN_BLOCK_MODE)
            .setEncryptionPaddings(KEYGEN_PADDING)
            .build()
        )
        generateKey()
      }
      Log.i(TAG, "Secret key with %s is created. $CIPHER_ALGORITHM")

      true
    } catch (e: GeneralSecurityException) {
      Log.w(TAG, "It seems that this device does not support latest algorithm!! , ${e.message}")
      false
    }
  }

  /**
   * Note that backed result with empty IV means an operation failure. It is a good idea to
   * check [isSupported] flag before invoking this method.
   */
  fun encrypt(plainText: String, key: String): EncryptionSpec {
    if (!_isSupported) {
      return EncryptionSpec(plainText, key)
    }

    val cipher = Cipher.getInstance(CIPHER_ALGORITHM).apply {
      init(Cipher.ENCRYPT_MODE, keyEntry.secretKey)
    }

    val result = cipher.doFinal(plainText.toByteArray())
    val iv = cipher.iv

    return EncryptionSpec(result.toBase64String(), iv.toBase64String())
  }

  /**
   * This method assumes that all parameters are encoded as Base64 encoding.
   */
  fun decrypt(spec: EncryptionSpec): String {
    if (!_isSupported) {
      return spec.cipherText
    }

    val base64DecodedCipherText = Base64.decode(spec.cipherText, Base64.DEFAULT)
    val base64DecodedIv = Base64.decode(spec.iv, Base64.DEFAULT)

    val cipher = Cipher.getInstance(CIPHER_ALGORITHM).apply {
      init(Cipher.DECRYPT_MODE, keyEntry.secretKey, IvParameterSpec(base64DecodedIv))
    }

    return String(cipher.doFinal(base64DecodedCipherText))
  }

  private fun ByteArray.toBase64String(): String =
    String(Base64.encode(this, Base64.DEFAULT))

  /**
   * Generates a randomly chosen secure key for encryption. However, as long as the result of
   * this method lives on memory, it could be a nice attack surface.
   *
   * Using this method is not good for a strong security measures, but if your 3rd party logic
   * does not supports Android KeyStore, you have a no choice.
   */
  /*fun generateRandomKey(lengthBits: Int): ByteArray {
    return with(KeyGenerator.getInstance("AES")) {
      init(lengthBits, SecureRandom())
      generateKey().encoded
    }
  }*/

  /**
   * All values of this class are Base64 encoded to ensure a safe Java String representation.
   */
  class EncryptionSpec(val cipherText: String, val iv: String)
}