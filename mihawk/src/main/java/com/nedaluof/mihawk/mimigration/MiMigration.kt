package com.nedaluof.mihawk.mimigration

/**
 * Created by NedaluOf on 12/12/2021.
 */
interface MiMigration {
  fun oldSharedPreferencesFileName(): String
  fun oldKeysToMigrate(): Set<String>
}