package com.nedaluof.mihawk.milogger

import android.util.Log
import com.nedaluof.mihawk.miutil.MiConstants

/**
 * Created by NedaluOf on 11/12/2021.
 */
class MiLoggerImpl : MiLogger {
  override fun info(message: String) {
    Log.i(MiConstants.LOG_I, message)
  }

  override fun error(message: String) {
    Log.e(MiConstants.LOG_E, message)
  }

  override fun debug(message: String) {
    Log.d(MiConstants.LOG_D, message)
  }
}