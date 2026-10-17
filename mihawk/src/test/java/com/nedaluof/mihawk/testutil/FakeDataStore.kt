package com.nedaluof.mihawk.testutil

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * In-memory [DataStore] double so preference logic can run on the JVM
 * without Android files or [androidx.datastore.preferences.preferencesDataStore].
 */
class FakeDataStore(
  initialPreferences: Preferences = preferencesOf()
) : DataStore<Preferences> {

  private var snapshot: Preferences = initialPreferences

  override val data: Flow<Preferences> = flow { emit(snapshot) }

  override suspend fun updateData(
    transform: suspend (t: Preferences) -> Preferences
  ): Preferences {
    snapshot = transform(snapshot)
    return snapshot
  }

  /**
   * Seeds a raw value under [key] bypassing MiHawk serialization,
   * emulating values migrated from SharedPreferences with their original types.
   * */
  @Suppress("UNCHECKED_CAST")
  fun seed(key: Preferences.Key<*>, value: Any) {
    val mutable = snapshot.toMutablePreferences()
    mutable[key as Preferences.Key<Any>] = value
    snapshot = mutable.toPreferences()
  }
}
