package com.nedaluof.mihawk.mipreparation

import androidx.annotation.WorkerThread

/**
 * Created by NedaluOf on 12/6/2021.
 */
interface MiPreparation {
  @WorkerThread
  fun <T> prepareDataToSet(
    key: String,
    value: T
  ): String

  @WorkerThread
  fun <T> prepareDataToGet(
    cipherText: String,
    aClass: Class<T>
  ): T?
}