package com.nedaluof.mihawk.mipreferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nedaluof.mihawk.mimigration.DefaultMiMigration
import com.nedaluof.mihawk.mimigration.MiMigration

/**
 * Created by NedaluOf on 5/27/2022.
 * Updated by NedaluOf on 19/09/2025.
 */
class MiDataStoreInitializer(
  context: Context,
  preferenceFileName: String,
  private val miMigration: MiMigration
) {
  //region logic
  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = preferenceFileName,
    produceMigrations = { context ->
      if (miMigration !is DefaultMiMigration) {
        listOf(
          SharedPreferencesMigration(
            context = context,
            sharedPreferencesName = miMigration.oldSharedPreferencesFileName(),
            keysToMigrate = miMigration.oldKeysToMigrate()
          )
        )
      } else emptyList()
    }
  )

  val dataStore: DataStore<Preferences> = context.dataStore
  //endregion
}