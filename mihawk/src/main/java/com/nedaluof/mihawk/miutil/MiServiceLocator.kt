package com.nedaluof.mihawk.miutil

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.nedaluof.mihawk.miencryption.MiEncryption
import com.nedaluof.mihawk.miencryption.MiEncryptionImpl
import com.nedaluof.mihawk.miencryption.MiNoEncryption
import com.nedaluof.mihawk.milogger.MiLogger
import com.nedaluof.mihawk.milogger.MiLoggerImpl
import com.nedaluof.mihawk.mipreferences.MiPreferences
import com.nedaluof.mihawk.mipreferences.MiPreferencesImpl
import com.nedaluof.mihawk.mipreparation.MiPreparation
import com.nedaluof.mihawk.mipreparation.MiPreparationImpl
import com.nedaluof.mihawk.miserializer.MiSerializer
import com.nedaluof.mihawk.miserializer.MiSerializerImpl

/**
 * Created by NedaluOf on 11/12/2021.
 */
object MiServiceLocator {

  fun provideMiLogger(): MiLogger = MiLoggerImpl(true)

  fun provideMiPreparation(context: Context): MiPreparation =
    MiPreparationImpl(provideMiSerializer(), provideMiEncryption(context))

  private fun provideMiSerializer(): MiSerializer = MiSerializerImpl(Gson())

  fun provideMiEncryption(context: Context): MiEncryption {
    var encryption: MiEncryption = MiEncryptionImpl(context)
    if (!encryption.initialized()) {
      encryption = MiNoEncryption()
    }
    return encryption
  }

  fun provideMiPreferences(
    context: Context,
    miPreparation: MiPreparation,
    miLogger: MiLogger
  ): MiPreferences = MiPreferencesImpl(
    context,
    log = miLogger,
    preparation = miPreparation
  )

  fun provideDataStore(context: Context) = context.dataStore

  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = MiConstants.STORAGE_TAG_DO_NOT_CHANGE,
    produceMigrations = ::sharedPreferencesMigration
  )

  private fun sharedPreferencesMigration(context: Context) =
    listOf(SharedPreferencesMigration(context, MiConstants.STORAGE_TAG_DO_NOT_CHANGE))
}