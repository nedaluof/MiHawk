package com.nedaluof.mihawk.mipreferences

import kotlinx.coroutines.flow.Flow

/**
 * Created by NedaluOf on 12/12/2021.
 */
interface MiPreferences {
  fun <T> putData(key: String, value: T)
  fun <T> getData(key: String, aClass: Class<T>): Flow<T?>
  suspend fun removeData(key: String, result: (Boolean) -> Unit)
  fun deleteAll(result: (Boolean) -> Unit)
  fun contains(key: String, result: (Boolean) -> Unit)
}