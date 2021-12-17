package com.nedaluof.mihawk.miserializer

import com.google.gson.Gson

/**
 * Created by NedaluOf on 11/10/2021.
 */
class MiSerializerImpl(
  private val gson: Gson
) : MiSerializer {

  override fun <T> toString(t: T): String = gson.toJson(t)

  override fun <T> fromString(value: String?, aClass: Class<T>): T? =
    gson.fromJson(value, aClass)
}