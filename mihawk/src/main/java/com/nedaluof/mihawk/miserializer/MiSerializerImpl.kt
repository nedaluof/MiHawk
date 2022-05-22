package com.nedaluof.mihawk.miserializer

import com.google.gson.Gson

/**
 * Created by NedaluOf on 11/10/2021.
 */
class MiSerializerImpl(
  private val gson: Gson
) : MiSerializer {

  override fun <T> toString(obj: T): String {
    return gson.toJson(obj)
  }

  override fun <T> fromString(json: String?, aClass: Class<T>): T? {
    return gson.fromJson(json, aClass)
  }
}
