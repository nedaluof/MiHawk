package com.nedaluof.mihawk.mifacade

import kotlinx.coroutines.flow.Flow

/**
 * Created by NedaluOf on 12/12/2021.
 */
interface MiHawkFacade {
  fun <T> putData(key: String, t: T)

  fun <T> getData(key: String, aClass: Class<T>): Flow<T?>

  fun removeData(key: String, result: (Boolean) -> Unit)

  fun contains(key: String, result: (Boolean) -> Unit)

  fun deleteAll(result: (Boolean) -> Unit)
}