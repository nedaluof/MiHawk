package com.nedaluof.mihawk.mipreferences

import android.content.Context
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
class MiPreferencesImpl(
  private val context: Context,
  private val dataStore: DataStore<Preferences> = MiServiceLocator.provideDataStore(context),
  private val preparation: MiPreparation = MiServiceLocator.provideMiPreparation(context),
  private val log: MiLogger = MiServiceLocator.provideMiLogger(),
  private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MiPreferences {

  override fun <T> putData(key: String, t: T) {
    runBlocking(dispatcher) {
      try {
        dataStore.edit { preferences ->
          preferences[key] = t
        }
        log.info("putData -> ${t.toString()}")
      } catch (e: Exception) {
        log.error("putData -> ${e.message!!}")
        e.printStackTrace()
      }
    }
  }

  override fun <T> getData(key: String, aClass: Class<T>): Flow<T?> = dataStore.data
    .map { preferences ->
      withContext(dispatcher) {
        try {
          val value = preferences[key, aClass]
          log.info("getData -> ${value.toString()}")
          value
        } catch (e: Exception) {
          log.error("getData -> ${e.message!!}")
          log.error("getData -> No data Stored With this key in MiHawk")
          null
        }
      }
    }

  override suspend fun removeData(key: String) {
    withContext(dispatcher) {
      try {
        log.info("removeData -> of key {$key}")
        dataStore.edit { preferences -> preferences.remove(key) }
        log.info("removeData -> removed successfully")
      } catch (e: Exception) {
        log.error("removeData -> ${e.message!!}")
        e.printStackTrace()
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
        log.info("deleteAll -> all data deleted successfully")
      } catch (e: Exception) {
        log.error("deleteAll -> ${e.message!!}")
        e.printStackTrace()
      }
    }
  }

  override fun contains(key: String, result: (Boolean) -> Unit) {
    runBlocking {
      try {
        dataStore.edit { result(it.contains(key)) }
        log.info("contains -> check if $key exist.")
      } catch (e: Exception) {
        log.error("deleteAll -> ${e.message!!}")
        e.printStackTrace()
      }
    }
  }


  private operator fun <T> MutablePreferences.set(
    key: String,
    value: T
  ) {
    val preparedValue = preparation.prepareDataToSet(key, value)
    set(stringPreferencesKey(key), preparedValue)
  }

  @Suppress("UNCHECKED_CAST")
  private operator fun <T> Preferences.get(
    key: String,
    aClass: Class<T>
  ): T? {
    val cipherText = get(stringPreferencesKey(key))!!
    return preparation.prepareDataToGet(Pair(key, cipherText), aClass)
      ?: get(stringPreferencesKey(key)) as T?
  }

  private fun MutablePreferences.remove(
    key: String
  ) = remove(stringPreferencesKey(key))


  private fun MutablePreferences.contains(
    key: String
  ): Boolean = contains(stringPreferencesKey(key))
}