package com.nedaluof.mihawk.miserializer

import androidx.annotation.WorkerThread

/**
 * Created by NedaluOf on 11/10/2021.
 */
interface MiSerializer {
  @WorkerThread
  fun <T> toString(obj: T): String

  @WorkerThread
  fun <T> fromString(json: String?, aClass: Class<T>): T?
}