package com.nedaluof.mihawk.milogger

import android.util.Log
import com.nedaluof.mihawk.BuildConfig
import com.nedaluof.mihawk.miutil.MiConstants

/**
 * Created by NedaluOf on 11/12/2021.
 */
class MiLoggerImpl(
  private val isLoggerEnabled: Boolean = true
) : MiLogger {
  override fun info(message: String) {
    checkIfDebug {
      Log.i(MiConstants.LOG_I, message)
    }
  }

  override fun error(message: String) {
    checkIfDebug {
      Log.e(MiConstants.LOG_E, message)
    }
  }

  override fun debug(message: String) {
    checkIfDebug {
      Log.d(MiConstants.LOG_D, message)
    }
  }

  private fun checkIfDebug(
    isInDebugMode: () -> Unit
  ) {
    if (isLoggerEnabled) {
      isInDebugMode()
    }
  }
}