@file:Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE", "unused")
package com.nedaluof.mihawk

import android.content.Context
import com.google.gson.Gson
import com.nedaluof.mihawk.miencryption.MiEncryption
import com.nedaluof.mihawk.mifacade.MiHawkFacade
import com.nedaluof.mihawk.mifacade.MiHawkFacadeImpl
import com.nedaluof.mihawk.milogger.MiLogger
import com.nedaluof.mihawk.mipreferences.MiPreferencesImpl
import com.nedaluof.mihawk.mipreparation.MiPreparation
import com.nedaluof.mihawk.mipreparation.MiPreparationImpl
import com.nedaluof.mihawk.miserializer.MiSerializer
import com.nedaluof.mihawk.miserializer.MiSerializerImpl
import com.nedaluof.mihawk.miutil.MiConstants
import com.nedaluof.mihawk.miutil.MiServiceLocator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference

/**
 * Created by NedaluOf on 11/10/2021.
 */
object MiHawk {

  private lateinit var miHawkFacade: MiHawkFacade

  /**
   * easy way to initialize MiHawk
   * without any customization
   * @param context
   * */
  fun init(context: Context) {
    miHawkFacade = Builder(context).build()
  }

  /**
   * Put data to be stored on MiHawk
   * @param key : key used to retrieve your data using it
   * @param value : value/data of what you want data class model
   *               or primitives or list .... etc.
   * */
  inline fun <reified T> put(key: String, value: T) {
    checkMiHawkFacade()
    miHawkFacade.putData(key, value)
  }

  /**
   * Get your data using :
   * @param key : that you using on [put] function
   * @param result : data returned over Higher-order function block
   *                this useful if you need to restrict the flow
   *                until the data retrieved , if there no
   *                data associated with the provided key
   *                {null} will be returned.
   * */
  inline fun <reified T> get(key: String, crossinline result: (T?) -> Unit) {
    checkMiHawkFacade()
    runBlocking {
      result(miHawkFacade.getData(key, T::class.java).first())
    }
  }

  /**
   * Get your data using
   * @param key : that you using on [put] function
   * @return T : data that you saved using [put] function
   *            if there no data associated with the provided
   *            key {null} will be returned.
   * */
  inline fun <reified T> get(key: String): T? {
    checkMiHawkFacade()
    return try {
      runBlocking {
        return@runBlocking miHawkFacade.getData(key, T::class.java).first()
      }
    } catch (e: Exception) {
      null
    }
  }

  /**
   * Get your data using :
   * @param key : that you using on [put] function
   * @param defaultValue : default value if no such data stored before
   *                      using this key.
   * @param result : data returned over Higher-order function block
   *                this useful if you need to restrict the flow
   *                until the data retrieved.
   * */
  inline fun <reified T> get(key: String, defaultValue: T, crossinline result: (T?) -> Unit) {
    checkMiHawkFacade()
    runBlocking {
      result(miHawkFacade.getData(key, T::class.java).first() ?: defaultValue)
    }
  }

  /**
   * Get your data using
   * @param key : that you using on [put] function
   * @param defaultValue : default value if no such data stored before
   *                      using this key.
   * @return T : data that you saved using [put] function
   *            if there no data associated with the provided
   *            key.
   * */
  inline fun <reified T> get(key: String, defaultValue: T): T {
    checkMiHawkFacade()
    return try {
      runBlocking {
        return@runBlocking miHawkFacade.getData(key, T::class.java).first() ?: defaultValue
      }
    } catch (e: Exception) {
      defaultValue
    }
  }

  /**
   * Remove data
   * @param key : remove data that associated with this key
   * @param result : result will be returned over Higher-order function block
   *                 , this return boolean to indicate that the data
   *                 removed successfully or not , in case of returned false
   *                 this indicates that the key already not have associated data.
   * */
  fun remove(key: String, result: (Boolean) -> Unit) {
    checkMiHawkFacade()
    miHawkFacade.removeData(key, result)
  }

  /**
   * Contains data
   * @param key : take the provided key and check if there data associated with it.
   * @param result : result will be returned over Higher-order function block
   *                 , this return boolean to indicate that there data associated
   *                 with the key or not.
   * */
  fun contains(key: String, result: (Boolean) -> Unit) {
    checkMiHawkFacade()
    miHawkFacade.contains(key, result)
  }

  /**
   * Delete All data
   * @param result : result will be returned over Higher-order function block
   *                 , this return boolean to indicate that the all data stored
   *                 in MiHawk deleted successfully or not.
   * */
  fun deleteAll(result: (Boolean) -> Unit) {
    checkMiHawkFacade()
    miHawkFacade.deleteAll(result)
  }

  /**
   * function to check if the MiHawk Facade initialized or not
   * */
  private fun checkMiHawkFacade() {
    if (!(this::miHawkFacade.isInitialized)) {
      throw RuntimeException("You Forgot To call MiHawk.init(context) or MiHawk.Builder().withContext(context:Context).build()")
    }
  }

  /**
   * MiHawk Builder
   * this [Builder] built to make
   * MiHawk more customizable , you can
   * adapt your logger , serializer , encryptor
   * , preferences file name , enable/disable
   * default MiHawk Logger
   * */
  class Builder(
    private val context: Context
  ) {

    private lateinit var miLogger: MiLogger
    private var miSerializer: MiSerializer = MiSerializerImpl(Gson())
    private lateinit var miEncryption: MiEncryption
    private lateinit var miPreparation: MiPreparation

    /**
     * @param preferenceFileName :  your preference file name
     * if not set ; then the default one will be used
     * @see [MiConstants.DEFAULT_PREFERENCE_FILE_NAME]
     * */
    fun withPreferenceName(preferenceFileName: String) = apply {
      MiServiceLocator.preferenceFileName = preferenceFileName
    }

    /**
     * @param isLoggerEnabled : indicate if logger enable
     * */
    fun withLoggingEnabled(isLoggerEnabled: Boolean) = apply {
      MiServiceLocator.isLoggerEnabled = isLoggerEnabled
    }

    fun withMiLogger(miLogger: MiLogger) = apply {
      this.miLogger = miLogger
    }

    fun withMiSerializer(miSerializer: MiSerializer) = apply {
      this.miSerializer = miSerializer
    }

    fun withMiEncryption(miEncryption: MiEncryption) = apply {
      this.miEncryption = miEncryption
    }

    fun build(): MiHawkFacade {
      val weakContext = WeakReference(context)
      val context = weakContext.get()!!
      val newFacade = MiHawkFacadeImpl(
        MiPreferencesImpl.buildMiPreferences(
          MiServiceLocator.provideDataStore(context),
          getMiPreparation(context),
          getMiLogger()
        )
      )
      miHawkFacade = newFacade
      return miHawkFacade
    }

    private fun getMiPreparation(context: Context): MiPreparation =
      if (!::miPreparation.isInitialized) {
        MiServiceLocator.provideMiPreparation(context)
      } else {
        if (!::miEncryption.isInitialized) {
          miEncryption = MiServiceLocator.provideMiEncryption(context)
        }
        MiPreparationImpl(miSerializer, miEncryption)
      }

    private fun getMiLogger(): MiLogger =
      if (!::miLogger.isInitialized) MiServiceLocator.provideMiLogger() else miLogger
  }
}