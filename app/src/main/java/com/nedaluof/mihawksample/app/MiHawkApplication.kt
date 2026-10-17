package com.nedaluof.mihawksample.app

import android.app.Application
import com.nedaluof.mihawk.MiHawk
import com.nedaluof.mihawk.mimigration.MiMigration
import com.nedaluof.mihawksample.data.PreferencesKeys

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
      .withLoggingEnabled(true)
      .withPreferenceName("MIHAWK_PREFS")
      .withMiMigration(object : MiMigration {
        override fun oldSharedPreferencesFileName(): String = "MI_PREFS"

        override fun oldKeysToMigrate(): Set<String> {
          return setOf(
            PreferencesKeys.USER_NAME,
            PreferencesKeys.USER_AGE,
            PreferencesKeys.USER_IS_FREE
          )
        }
      }).build()
  }
}