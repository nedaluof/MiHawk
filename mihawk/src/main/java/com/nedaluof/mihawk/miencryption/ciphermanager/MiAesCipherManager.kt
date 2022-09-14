package com.nedaluof.mihawk.miencryption.ciphermanager

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.nedaluof.mihawk.miencryption.model.MiEncryptionSpec
import com.nedaluof.mihawk.miutil.MiConstants.CIPHER_ALGORITHM
import com.nedaluof.mihawk.miutil.MiConstants.KEYGEN_ALGORITHM
import com.nedaluof.mihawk.miutil.MiConstants.KEYGEN_BLOCK_MODE
import com.nedaluof.mihawk.miutil.MiConstants.KEYGEN_PADDING
import com.nedaluof.mihawk.miutil.MiConstants.KEY_LENGTH_BITS
import com.nedaluof.mihawk.miutil.MiConstants.KEY_PROVIDER_NAME
import com.nedaluof.mihawk.miutil.MiServiceLocator
import java.security.GeneralSecurityException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec

/**
 * Created by NedaluOf on 6/11/2022.
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
 */
object MiAesCipherManager {

  /**
   * Todo: transform to normal class to avoid any memory leak
   * */
  private val logger = MiServiceLocator.provideMiLogger()

  private lateinit var keyEntry: KeyStore.SecretKeyEntry

  private lateinit var keygen: KeyGenerator

  /**
   * indicate that the algorithm is initialized
   * */
  var isSupported = false
    private set

  internal fun init(applicationContext: Context) {
    if (isSupported) {
      logger.info( "Already initialised - Do not attempt to initialise this twice")
      return
    }

    try {
      this.keygen = KeyGenerator.getInstance(KEYGEN_ALGORITHM, KEY_PROVIDER_NAME)
    } catch (e: GeneralSecurityException) {
      this.isSupported = false
      // Nonsense, but happens on low-end devices such as Xiaomi
      logger.info("It seems that this device does not supports AES and/or RSA encryption.")
      return
    }

    val alias = "${applicationContext.packageName}.aeskey"
    val keyStore = KeyStore.getInstance(KEY_PROVIDER_NAME).apply {
      load(null)
    }

    val result = if (keyStore.containsAlias(alias)) {
      logger.info("Secret key for %s exists, loading previously created one , $alias")
      true
    } else {
      logger.info("No secret key for %s, creating a new one, $alias")
      initAndroidM(alias)
    }

    this.keyEntry = keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry
    this.isSupported = result
  }

  private fun initAndroidM(alias: String): Boolean {
    return try {
      with(keygen) {
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
      logger.info("Secret key with %s is created. $CIPHER_ALGORITHM")
      true
    } catch (e: GeneralSecurityException) {
      logger.info("It seems that this device does not support latest algorithm!! , ${e.message}")
      false
    }
  }

  /**
   * Note that backed result with empty IV means an operation failure. It is a good idea to
   * check [isSupported] flag before invoking this method.
   */
  fun encrypt(plainText: String, key: String): MiEncryptionSpec {
    if (!isSupported) {
      return MiEncryptionSpec(plainText, key)
    }

    val cipher = Cipher.getInstance(CIPHER_ALGORITHM).apply {
      init(Cipher.ENCRYPT_MODE, keyEntry.secretKey)
    }

    val result = cipher.doFinal(plainText.toByteArray())
    val iv = cipher.iv

    return MiEncryptionSpec(result.toBase64String(), iv.toBase64String())
  }

  /**
   * This method assumes that all parameters are encoded as Base64 encoding.
   */
  fun decrypt(spec: MiEncryptionSpec): String {
    if (!isSupported) {
      return spec.cipherText
    }

    val base64DecodedCipherText = Base64.decode(spec.cipherText, Base64.DEFAULT)
    val base64DecodedIv = Base64.decode(spec.key, Base64.DEFAULT)

    val cipher = Cipher.getInstance(CIPHER_ALGORITHM).apply {
      init(Cipher.DECRYPT_MODE, keyEntry.secretKey, IvParameterSpec(base64DecodedIv))
    }

    return String(cipher.doFinal(base64DecodedCipherText))
  }

  private fun ByteArray.toBase64String(): String =
    String(Base64.encode(this, Base64.DEFAULT))
}