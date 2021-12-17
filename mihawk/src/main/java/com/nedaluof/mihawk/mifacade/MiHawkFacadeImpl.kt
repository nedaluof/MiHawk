package com.nedaluof.mihawk.mifacade

import com.nedaluof.mihawk.mipreferences.MiPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

/**
 * Created by NedaluOf on 11/10/2021.
 */
class MiHawkFacadeImpl(
  private val preferences: MiPreferences
) : MiHawkFacade {

  override fun <T> putData(key: String, t: T) = preferences.putData(key, t)

  override fun <T> getData(key: String, aClass: Class<T>): Flow<T?> =
    preferences.getData(key, aClass)

  override fun removeData(key: String, result: (Boolean) -> Unit) = runBlocking {
    preferences.removeData(key)
    result(true)
  }

  override fun contains(key: String, result: (Boolean) -> Unit) =
    preferences.contains(key, result)

  override fun deleteAll(result: (Boolean) -> Unit) =
    preferences.deleteAll(result)
}