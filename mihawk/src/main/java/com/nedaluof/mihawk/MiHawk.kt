@file:Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package com.nedaluof.mihawk

import android.content.Context
import com.google.gson.Gson
import com.nedaluof.mihawk.miencryption.MiEncryption
import com.nedaluof.mihawk.mifacade.MiHawkFacade
import com.nedaluof.mihawk.mifacade.MiHawkFacadeImpl
import com.nedaluof.mihawk.milogger.MiLogger
import com.nedaluof.mihawk.milogger.MiLoggerImpl
import com.nedaluof.mihawk.mipreparation.MiPreparation
import com.nedaluof.mihawk.mipreparation.MiPreparationImpl
import com.nedaluof.mihawk.miserializer.MiSerializer
import com.nedaluof.mihawk.miserializer.MiSerializerImpl
import com.nedaluof.mihawk.miutil.MiServiceLocator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference

/**
 * Created by NedaluOf on 11/10/2021.
 */
object MiHawk {
  private lateinit var miHawkFacade: MiHawkFacade

  fun init(context: Context) {
   miHawkFacade = Builder().withContext(context).build()
    /*miHawkFacade = MiHawkFacadeImpl(
      MiServiceLocator.provideMiPreferences(
        context,
        MiServiceLocator.provideMiPreparation(context),
        MiServiceLocator.provideMiLogger()
      )
    )*/
  }

  inline fun <reified T> put(key: String, value: T) {
    checkMiHawkFacade()
    miHawkFacade.putData(key, value)
  }

  inline fun <reified T> get(key: String, crossinline result: (T?) -> Unit) {
    checkMiHawkFacade()
    runBlocking {
      result(miHawkFacade.getData(key, T::class.java).first())
    }
  }

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

  inline fun <reified T> get(key: String, defaultValue: T, crossinline result: (T?) -> Unit) {
    checkMiHawkFacade()
    runBlocking {
      result(miHawkFacade.getData(key, T::class.java).first() ?: defaultValue)
    }
  }

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

  fun remove(key: String, result: (Boolean) -> Unit) {
    checkMiHawkFacade()
    miHawkFacade.removeData(key, result)
  }

  fun contains(key: String, result: (Boolean) -> Unit) {
    checkMiHawkFacade()
    miHawkFacade.contains(key, result)
  }

  fun deleteAll(result: (Boolean) -> Unit) {
    checkMiHawkFacade()
    miHawkFacade.deleteAll(result)
  }

  private fun checkMiHawkFacade() {
    if (!(this::miHawkFacade.isInitialized)) {
      throw RuntimeException("You Forgot To call MiHawk.init(context)")
    }
  }

  /**
   * MiHawk Builder
   * */
  class Builder {
    private lateinit var weakContext: WeakReference<Context>
    private var isLoggerEnabled: Boolean = true
    private var miLogger: MiLogger = MiLoggerImpl(isLoggerEnabled)
    private var miSerializer: MiSerializer = MiSerializerImpl(Gson())
    private lateinit var miEncryption: MiEncryption
    private lateinit var miPreparation: MiPreparation

    fun withContext(context: Context) = apply {
      this.weakContext = WeakReference(context)
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
      val context = weakContext.get()!!
      val facade: MiHawkFacade?
      if (::weakContext.isInitialized) {
        miPreparation = if (!::miPreparation.isInitialized) {
          MiServiceLocator.provideMiPreparation(context)
        } else {
          getMiPreparation()
        }
        facade = MiHawkFacadeImpl(
          MiServiceLocator.provideMiPreferences(
            context,
            miPreparation,
            miLogger
          )
        )
      } else {
        throw IllegalStateException("withContext(context:Context) must be called before build()")
      }
      return facade
    }

    private fun getMiPreparation(): MiPreparation {
      if (!::miEncryption.isInitialized) {
        miEncryption = MiServiceLocator.provideMiEncryption(weakContext.get()!!)
      }
      return MiPreparationImpl(miSerializer, miEncryption)
    }
  }
}