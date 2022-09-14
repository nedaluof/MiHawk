package com.nedaluof.mihawk.mipreferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nedaluof.mihawk.miutil.MiConstants

/**
 * Created by NedaluOf on 5/27/2022.
 * *********************************************
 * Todo: Migration strategy for people whose
 *       need to use MiHawk in existing projects
 *       that use shared preference.
 */
class MiDataStoreInitializer(
  context: Context,
  preferenceFileName: String
) {

  private fun sharedPreferencesMigration(context: Context) =
    listOf(SharedPreferencesMigration(context, MiConstants.DEFAULT_PREFERENCE_FILE_NAME))

  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = preferenceFileName,
    produceMigrations = ::sharedPreferencesMigration
  )

  val dataStore: DataStore<Preferences> = context.dataStore
}