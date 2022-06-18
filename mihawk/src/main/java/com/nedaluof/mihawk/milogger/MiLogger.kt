package com.nedaluof.mihawk.milogger

/**
 * Created by NedaluOf on 11/12/2021.
 */
interface MiLogger {
  fun info(message: String)
  fun error(message: String)
  fun debug(message: String)
}