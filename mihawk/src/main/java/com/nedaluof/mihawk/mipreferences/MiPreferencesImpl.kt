package com.nedaluof.mihawk.mipreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nedaluof.mihawk.milogger.MiLogger
import com.nedaluof.mihawk.mipreparation.MiPreparation
import com.nedaluof.mihawk.miutil.MiServiceLocator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * Created by NedaluOf on 11/10/2021.
 */
class MiPreferencesImpl internal constructor(
  private val dataStore: DataStore<Preferences>,
  private val miPreparation: MiPreparation,
  private val miLogger: MiLogger,
  private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MiPreferences {

  //region MiPreferences implementation
  override fun <T> putData(key: String, value: T) {
    runBlocking(dispatcher) {
      try {
        dataStore.edit { preferences ->
          preferences[key] = value
        }
        checkIfIsLoggerEnabled { miLogger.info("putData -> ${value.toString()}") }
      } catch (e: Exception) {
        checkIfIsLoggerEnabled { miLogger.error("putData -> ${e.message!!}") }
        e.printStackTrace()
      }
    }
  }

  override fun <T> getData(key: String, aClass: Class<T>): Flow<T?> = dataStore.data
    .map { preferences ->
      withContext(dispatcher) {
        try {
          val value = preferences[key, aClass]
          checkIfIsLoggerEnabled { miLogger.info("getData -> ${value.toString()}") }
          value
        } catch (e: Exception) {
          checkIfIsLoggerEnabled { miLogger.error("getData -> ${e.message!!}") }
          checkIfIsLoggerEnabled { miLogger.error("getData -> No data Stored With this key in MiHawk") }
          null
        }
      }
    }

  override suspend fun removeData(key: String, result: (Boolean) -> Unit) {
    withContext(dispatcher) {
      try {
        checkIfIsLoggerEnabled { miLogger.info("removeData -> of key {$key}") }
        dataStore.edit { preferences -> preferences.remove(key) }
        checkIfIsLoggerEnabled { miLogger.info("removeData -> removed successfully") }
        result(true)
      } catch (e: Exception) {
        checkIfIsLoggerEnabled { miLogger.error("removeData -> ${e.message!!}") }
        result(false)
      }
    }
  }

  override fun deleteAll(result: (Boolean) -> Unit) {
    runBlocking {
      try {
        dataStore.edit { preferences ->
          preferences.clear()
          result(true)
        }
        checkIfIsLoggerEnabled { miLogger.info("deleteAll -> all data deleted successfully") }
      } catch (e: Exception) {
        checkIfIsLoggerEnabled { miLogger.error("deleteAll -> ${e.message!!}") }
        e.printStackTrace()
      }
    }
  }

  override fun contains(key: String, result: (Boolean) -> Unit) {
    runBlocking {
      try {
        dataStore.edit { result(it.contains(key)) }
        checkIfIsLoggerEnabled { miLogger.info("contains -> check if $key exist.") }
      } catch (e: Exception) {
        checkIfIsLoggerEnabled { miLogger.error("deleteAll -> ${e.message!!}") }
        e.printStackTrace()
      }
    }
  }

  //endregion

  /**
   * DataStore operators utils.
   * */
  //region operators
  private operator fun <T> MutablePreferences.set(
    key: String,
    value: T
  ) {
    val preparedValue = miPreparation.prepareDataToSet(key, value)
    set(stringPreferencesKey(key), preparedValue)
  }

  @Suppress("UNCHECKED_CAST")
  private operator fun <T> Preferences.get(
    key: String,
    aClass: Class<T>
  ): T? {
    val cipherText = get(stringPreferencesKey(key))!!
    return miPreparation.prepareDataToGet(Pair(key, cipherText), aClass)
      ?: get(stringPreferencesKey(key)) as T?
  }

  private fun MutablePreferences.remove(
    key: String
  ) = remove(stringPreferencesKey(key))

  private fun MutablePreferences.contains(
    key: String
  ): Boolean = contains(stringPreferencesKey(key))
  //endregion

  /**
   * Checking if logger enabled
   * */
  private fun checkIfIsLoggerEnabled(
    loggerEnabled: () -> Unit
  ) {
    if (MiServiceLocator.isLoggerEnabled) {
      loggerEnabled()
    }
  }

  companion object {
    fun buildMiPreferences(
      dataStore: DataStore<Preferences>,
      miPreparation: MiPreparation,
      miLogger: MiLogger
    ): MiPreferences = MiPreferencesImpl(
      dataStore,
      miPreparation,
      miLogger
    )
  }
}