package com.nedaluof.mihawk.mimigration

/**
 * Created by NedaluOf on 19/09/2025
 */
interface MiMigration {
  fun oldSharedPreferencesFileName(): String
  fun oldKeysToMigrate(): Set<String>
}