package com.nedaluof.mihawk.milogger

import com.nedaluof.mihawk.miutil.MiConstants

/**
 * Created by NedaluOf on 11/12/2021.
 */
object MiUnitTestLoggerImpl : MiLogger {
  override fun info(message: String) {
    println("${MiConstants.LOG_I} $message")
  }

  override fun error(message: String) {
    println("${MiConstants.LOG_E} $message")
  }

  override fun debug(message: String) {
    println("${MiConstants.LOG_D} $message")
  }
}