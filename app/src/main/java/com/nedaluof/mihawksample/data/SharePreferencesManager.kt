package com.nedaluof.mihawksample.data

import android.content.Context
import android.util.Log
import androidx.core.content.edit

/**
 * Created By NedaluOf - 19/09/2025.
 */
class SharePreferencesManager(
  private val context: Context
) {

  //region logic
  fun setValuesToSharedPreferences() {
    val prefs = context.getSharedPreferences("MI_PREFS", Context.MODE_PRIVATE)
    prefs.edit {
      putString(PreferencesKeys.USER_NAME, "NedaluOf")
      putInt(PreferencesKeys.USER_AGE, 29)
      putBoolean(PreferencesKeys.USER_IS_FREE, true)
    }
    getValuesToSharedPreferences()
  }

  fun getValuesToSharedPreferences() {
    val prefs = context.getSharedPreferences("MI_PREFS", Context.MODE_PRIVATE)
    Log.e(
      "NedaluOf", """
        OLD PREFS
            ${prefs.getString(PreferencesKeys.USER_NAME, "")}
            ${prefs.getInt(PreferencesKeys.USER_AGE, 0)}
            ${prefs.getBoolean(PreferencesKeys.USER_IS_FREE, false)}
    """.trimIndent()
    )
  }
  //endregion
}