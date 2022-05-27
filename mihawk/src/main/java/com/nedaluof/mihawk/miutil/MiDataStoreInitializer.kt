package com.nedaluof.mihawk.miutil

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * Created by NedaluOf on 5/27/2022.
 */
class MiDataStoreInitializer(context: Context, preferenceFileName: String) {
  private fun sharedPreferencesMigration(context: Context) =
    listOf(SharedPreferencesMigration(context, MiConstants.PREFERENCE_FILE_NAME_DO_NOT_CHANGE))

  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = preferenceFileName,
    produceMigrations = ::sharedPreferencesMigration
  )

  val dataStore: DataStore<Preferences> = context.dataStore
}