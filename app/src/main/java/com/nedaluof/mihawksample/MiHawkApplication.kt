package com.nedaluof.mihawksample

import android.app.Application
import com.nedaluof.mihawk.MiHawk

/**
 * Created By NedaluOf - 04/05/2025.
 */
class MiHawkApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    initMiHawk()
  }

  private fun initMiHawk() {
    MiHawk.Builder(this)
      .withPreferenceName("MIHAWK_PREFS")
      .build()
  }
}